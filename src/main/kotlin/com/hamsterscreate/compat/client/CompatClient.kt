package com.hamsterscreate.compat.client

import com.hamsterscreate.compat.HamstersCreateCompat
import com.hamsterscreate.compat.kinetics.HamsterWheelKineticBlockEntity
import com.starfish_studios.hamsters.registry.HamstersBlockEntities
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.ModLoadingContext
import java.util.function.Supplier

object CompatClient {
    private val renderer = HamsterWheelKineticRenderer()

    @JvmStatic
    fun registerConfigScreen() {
        val factory = Supplier {
            ConfigScreenHandler.ConfigScreenFactory { _, parent -> CompatConfigScreen(parent) }
        }
        val container = try {
            ModList.get().getModContainerById(HamstersCreateCompat.MOD_ID).orElse(null)
        } catch (_: Throwable) {
            null
        }
        if (container != null) {
            container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java, factory)
        } else {
            ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory::class.java,
                factory
            )
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
