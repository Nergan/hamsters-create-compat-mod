package com.hamsterscreate.compat.mixin;

import com.hamsterscreate.compat.client.CompatClient;
import com.starfish_studios.hamsters.events.ClientEvents;
import net.minecraftforge.client.event.EntityRenderersEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientEvents.class, remap = false)
public abstract class HamstersClientEventsMixin {
    @Inject(method = "registerRenderers", at = @At("TAIL"))
    private static void hamsterscreatecompat$replaceRenderer(
        EntityRenderersEvent.RegisterRenderers event,
        CallbackInfo ci
    ) {
        CompatClient.replaceWheelRenderer(event);
    }
}
