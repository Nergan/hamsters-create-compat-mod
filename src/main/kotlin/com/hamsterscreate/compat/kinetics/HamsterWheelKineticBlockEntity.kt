package com.hamsterscreate.compat.kinetics

import com.hamsterscreate.compat.advancement.ExploitationTrigger
import com.hamsterscreate.compat.config.CompatServerConfig
import com.hamsterscreate.compat.logic.ExploitationAdvancement
import com.hamsterscreate.compat.logic.WheelKinetics
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity
import com.simibubi.create.content.kinetics.base.IRotate
import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import com.starfish_studios.hamsters.block.HamsterWheelBlock
import com.starfish_studios.hamsters.entity.Hamster
import com.starfish_studios.hamsters.entity.SeatEntity
import com.starfish_studios.hamsters.registry.HamstersBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
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
    private var lastShaftConnected: Boolean = false

    override fun getGeneratedSpeed(): Float {
        val world = level ?: return 0f
        val facing = blockState.getValue(HamsterWheelBlock.FACING).toCompat()
        val occupant = seatedHamster()
        val rpm = CompatServerConfig.generatedRpm() * WheelKinetics.variantScale(occupant?.variant?.name.orEmpty())
        return WheelKinetics.generatedSpeed(
            HamsterWheelBlock.isOccupied(world, blockPos),
            rpm,
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
        val connected = hasConnectedShaft()
        if (speed != lastGeneratedSpeed && !world.isClientSide) {
            updateGeneratedRotation()
        }
        if (
            !world.isClientSide &&
            ExploitationAdvancement.becameEligible(lastGeneratedSpeed, lastShaftConnected, speed, connected)
        ) {
            ExploitationTrigger.awardNearby(world as ServerLevel, blockPos)
        }
        lastGeneratedSpeed = speed
        lastShaftConnected = connected
    }

    private fun seatedHamster(): Hamster? {
        val world = level ?: return null
        return world.getEntitiesOfClass(SeatEntity::class.java, AABB(blockPos))
            .firstOrNull()
            ?.firstPassenger as? Hamster
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
