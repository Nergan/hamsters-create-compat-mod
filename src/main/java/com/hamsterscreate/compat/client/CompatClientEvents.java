package com.hamsterscreate.compat.client;

import com.hamsterscreate.compat.HamstersCreateCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(
    modid = HamstersCreateCompat.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT
)
public final class CompatClientEvents {
    private CompatClientEvents() {
    }

    @SubscribeEvent
    public static void onConstruct(FMLConstructModEvent event) {
        CompatClient.registerConfigScreen();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CompatClient.registerConfigScreen();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        CompatClient.replaceWheelRenderer(event);
    }
}
