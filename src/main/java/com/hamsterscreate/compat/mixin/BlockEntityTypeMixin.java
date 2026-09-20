package com.hamsterscreate.compat.mixin;

import com.hamsterscreate.compat.kinetics.HamsterWheelKineticBlockEntity;
import com.starfish_studios.hamsters.registry.HamstersBlockEntities;
import com.starfish_studios.hamsters.registry.HamstersBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockEntityType.class, remap = true)
public abstract class BlockEntityTypeMixin {
    @Inject(
        method = "create(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/entity/BlockEntity;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hamsterscreatecompat$replaceWheelEntity(
        BlockPos pos,
        BlockState state,
        CallbackInfoReturnable<BlockEntity> cir
    ) {
        if (state.getBlock() != HamstersBlocks.HAMSTER_WHEEL.get()) {
            return;
        }
        if ((Object) this != HamstersBlockEntities.HAMSTER_WHEEL.get()) {
            return;
        }
        cir.setReturnValue(new HamsterWheelKineticBlockEntity(pos, state));
    }
}
