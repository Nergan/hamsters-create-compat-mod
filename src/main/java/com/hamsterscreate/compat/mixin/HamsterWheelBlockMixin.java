package com.hamsterscreate.compat.mixin;

import com.hamsterscreate.compat.interaction.HamsterWheelInteractions;
import com.hamsterscreate.compat.logic.WheelKinetics;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntityTicker;
import com.starfish_studios.hamsters.block.HamsterWheelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HamsterWheelBlock.class, remap = true)
public abstract class HamsterWheelBlockMixin extends BaseEntityBlock implements IRotate {
    protected HamsterWheelBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return WheelKinetics.hasShaftTowards(
            com.hamsterscreate.compat.kinetics.DirectionMapping.toCompat(state.getValue(HamsterWheelBlock.FACING)),
            com.hamsterscreate.compat.kinetics.DirectionMapping.toCompat(face)
        );
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HamsterWheelBlock.FACING).getAxis();
    }

    @Override
    public boolean showCapacityWithAnnotation() {
        return true;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof KineticBlockEntity kinetic) {
            kinetic.preventSpeedUpdate = 0;
            if (oldState.getBlock() == state.getBlock()
                && oldState.hasBlockEntity() == state.hasBlockEntity()
                && oldState.getValue(HamsterWheelBlock.FACING).getAxis() == state.getValue(HamsterWheelBlock.FACING).getAxis()) {
                kinetic.preventSpeedUpdate = 2;
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int flags, int count) {
        if (world.isClientSide()) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof KineticBlockEntity kinetic) || kinetic.preventSpeedUpdate > 0) {
            return;
        }
        kinetic.warnOfMovement();
        kinetic.clearKineticInformation();
        kinetic.updateSpeed = true;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return new SmartBlockEntityTicker<>();
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void hamsterscreatecompat$handleHamsterItem(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hit,
        CallbackInfoReturnable<InteractionResult> cir
    ) {
        InteractionResult result = HamsterWheelInteractions.handleUse(state, level, pos, player, hand);
        if (result != null) {
            cir.setReturnValue(result);
        }
    }
}
