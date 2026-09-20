package com.hamsterscreate.compat.kinetics

import com.hamsterscreate.compat.advancement.ExploitationTrigger
import com.hamsterscreate.compat.config.CompatServerConfig
import com.hamsterscreate.compat.logic.ExploitationAdvancement
import com.hamsterscreate.compat.logic.WheelKinetics
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity
import com.simibubi.create.content.kinetics.base.IRotate
import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import com.starfish_studios.hamsters.block.HamsterWheelBlock
import com.starfish_studios.hamsters.registry.HamstersBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import software.bernie.geckolib.animatable.GeoBlockEntity
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.core.animation.AnimationController
import software.bernie.geckolib.core.animation.AnimationState
import software.bernie.geckolib.core.animation.RawAnimation
import software.bernie.geckolib.core.`object`.PlayState
import software.bernie.geckolib.util.GeckoLibUtil

class HamsterWheelKineticBlockEntity(pos: BlockPos, state: BlockState) :
    GeneratingKineticBlockEntity(type(), pos, state),
    GeoBlockEntity {

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
    private var lastGeneratedSpeed: Float = Float.NaN

    override fun getGeneratedSpeed(): Float {
        val world = level ?: return 0f
        val facing = blockState.getValue(HamsterWheelBlock.FACING).toCompat()
        return WheelKinetics.generatedSpeed(
            HamsterWheelBlock.isOccupied(world, blockPos),
            CompatServerConfig.generatedRpm(),
            facing
        )
    }

    override fun calculateAddedStressCapacity(): Float {
        val capacity = CompatServerConfig.stressCapacityPerRpm().toFloat()
        lastCapacityProvided = capacity
        return capacity
    }

    override fun tick() {
        super.tick()
        val world = level ?: return
        val speed = generatedSpeed
        if (speed != lastGeneratedSpeed) {
            if (!world.isClientSide) {
                updateGeneratedRotation()
                if (ExploitationAdvancement.shouldAward(speed, hasConnectedShaft())) {
                    ExploitationTrigger.awardNearby(world as ServerLevel, blockPos)
                }
            }
            lastGeneratedSpeed = speed
        }
    }

    private fun hasConnectedShaft(): Boolean {
        val world = level ?: return false
        val facing = blockState.getValue(HamsterWheelBlock.FACING)
        for (face in Direction.entries) {
            if (!WheelKinetics.hasShaftTowards(facing.toCompat(), face.toCompat())) {
                continue
            }
            val neighbor = world.getBlockEntity(blockPos.relative(face))
            if (neighbor is KineticBlockEntity) {
                val neighborState = neighbor.blockState
                val neighborBlock = neighborState.block
                if (neighborBlock is IRotate && neighborBlock.hasShaftTowards(world, neighbor.blockPos, neighborState, face.opposite)) {
                    return true
                }
            }
        }
        return false
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(AnimationController(this, "controller", 0, ::controller))
    }

    private fun controller(event: AnimationState<HamsterWheelKineticBlockEntity>): PlayState {
        val world = level ?: return PlayState.STOP
        if (HamsterWheelBlock.isOccupied(world, blockPos)) {
            event.controller.setAnimation(SPIN)
            return PlayState.CONTINUE
        }
        return PlayState.STOP
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache

    companion object {
        private val SPIN: RawAnimation =
            RawAnimation.begin().thenLoop("animation.sf_nba.hamster_wheel.spin")

        @Suppress("UNCHECKED_CAST")
        private fun type(): BlockEntityType<HamsterWheelKineticBlockEntity> {
            return HamstersBlockEntities.HAMSTER_WHEEL.get() as BlockEntityType<HamsterWheelKineticBlockEntity>
        }
    }
}
