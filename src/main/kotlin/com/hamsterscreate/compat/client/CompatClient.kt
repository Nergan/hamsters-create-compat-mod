package com.hamsterscreate.compat.client

import com.hamsterscreate.compat.kinetics.HamsterWheelKineticBlockEntity
import com.starfish_studios.hamsters.registry.HamstersBlockEntities
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.client.event.EntityRenderersEvent

object CompatClient {
    private val renderer = HamsterWheelKineticRenderer()

    @JvmStatic
    fun wheelRenderer(): HamsterWheelKineticRenderer = renderer

    @JvmStatic
    fun replaceWheelRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        @Suppress("UNCHECKED_CAST")
        val type = HamstersBlockEntities.HAMSTER_WHEEL.get() as BlockEntityType<HamsterWheelKineticBlockEntity>
        event.registerBlockEntityRenderer(type) { renderer }
    }
}
