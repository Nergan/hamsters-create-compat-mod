package com.hamsterscreate.compat

import com.hamsterscreate.compat.kinetics.HamsterWheelStress
import com.starfish_studios.hamsters.registry.HamstersBlocks
import net.minecraft.world.level.block.Block
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.loading.FMLEnvironment

object ModSetup {
    fun init(modBus: IEventBus, container: ModContainer) {
        modBus.addListener(::onCommonSetup)
        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.hamsterscreate.compat.client.CompatClient.init(modBus, container)
        }
    }

    private fun onCommonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            HamsterWheelStress.register(HamstersBlocks.HAMSTER_WHEEL.get() as Block)
        }
    }
}
