package com.hamsterscreate.compat.mixin;

import com.hamsterscreate.compat.interaction.HamsterWheelInteractions;
import com.starfish_studios.hamsters.entity.Hamster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Hamster.class, remap = true)
public abstract class HamsterMixin {
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void hamsterscreatecompat$keepInWheel(CallbackInfo ci) {
        HamsterWheelInteractions.keepHamsterInWheel((Hamster) (Object) this);
    }
}
