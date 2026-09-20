package com.hamsterscreate.compat.client;

import com.hamsterscreate.compat.HamstersCreateCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
    modid = HamstersCreateCompat.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT
)
public final class CompatClientEvents {
    private CompatClientEvents() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        CompatClient.replaceWheelRenderer(event);
    }
}
