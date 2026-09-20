package com.hamsterscreate.compat.client

import com.hamsterscreate.compat.kinetics.HamsterWheelKineticBlockEntity
import com.starfish_studios.hamsters.registry.HamstersBlockEntities
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.world.level.block.entity.BlockEntityType

object CompatClient {
    @JvmStatic
    fun replaceWheelRenderer() {
        @Suppress("UNCHECKED_CAST")
        val type = HamstersBlockEntities.HAMSTER_WHEEL.get() as BlockEntityType<HamsterWheelKineticBlockEntity>
        BlockEntityRenderers.register(type) { HamsterWheelKineticRenderer() }
    }
}
