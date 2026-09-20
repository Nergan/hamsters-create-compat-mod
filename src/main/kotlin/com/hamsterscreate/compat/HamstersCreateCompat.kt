package com.hamsterscreate.compat

import com.hamsterscreate.compat.advancement.ExploitationTrigger
import com.hamsterscreate.compat.config.CompatServerConfig
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

@Mod(HamstersCreateCompat.MOD_ID)
class HamstersCreateCompat {
    companion object {
        const val MOD_ID: String = "hamsterscreatecompat"
    }

    init {
        val context = ModLoadingContext.get()
        val container = context.activeContainer
        context.registerConfig(
            ModConfig.Type.SERVER,
            CompatServerConfig.SPEC,
            CompatServerConfig.FILE_NAME
        )
        ModSetup.init(FMLJavaModLoadingContext.get().modEventBus, container)
        ExploitationTrigger.register()
    }
}
