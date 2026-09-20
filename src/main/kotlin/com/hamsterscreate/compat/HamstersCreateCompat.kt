package com.hamsterscreate.compat

import com.hamsterscreate.compat.advancement.ExploitationTrigger
import com.hamsterscreate.compat.config.CompatServerConfig
import com.hamsterscreate.compat.kinetics.HamsterWheelStress
import com.starfish_studios.hamsters.registry.HamstersBlocks
import net.minecraft.world.level.block.Block
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(HamstersCreateCompat.MOD_ID)
object HamstersCreateCompat {
    const val MOD_ID: String = "hamsterscreatecompat"

    init {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CompatServerConfig.SPEC)
        MOD_BUS.addListener(::onCommonSetup)
        ExploitationTrigger.register()
    }

    private fun onCommonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            HamsterWheelStress.register(HamstersBlocks.HAMSTER_WHEEL.get() as Block)
        }
    }
}
