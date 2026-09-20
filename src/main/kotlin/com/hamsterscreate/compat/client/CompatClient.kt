package com.hamsterscreate.compat.client

import com.hamsterscreate.compat.kinetics.HamsterWheelKineticBlockEntity
import com.starfish_studios.hamsters.registry.HamstersBlockEntities
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModContainer

object CompatClient {
    private val renderer = HamsterWheelKineticRenderer()

    @JvmStatic
    fun init(modBus: IEventBus, container: ModContainer) {
        CompatConfigScreens.register(container)
        modBus.addListener(EventPriority.LOWEST) { event: EntityRenderersEvent.RegisterRenderers ->
            replaceWheelRenderer(event)
        }
    }

    @JvmStatic
    fun wheelRenderer(): HamsterWheelKineticRenderer = renderer

    @JvmStatic
    fun replaceWheelRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        @Suppress("UNCHECKED_CAST")
        val type = HamstersBlockEntities.HAMSTER_WHEEL.get() as BlockEntityType<HamsterWheelKineticBlockEntity>
        event.registerBlockEntityRenderer(type) { renderer }
    }
}
