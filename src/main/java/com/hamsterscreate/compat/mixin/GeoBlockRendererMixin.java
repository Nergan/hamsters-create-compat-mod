package com.hamsterscreate.compat.mixin;

import com.hamsterscreate.compat.client.CompatClient;
import com.hamsterscreate.compat.client.HamsterWheelKineticRenderer;
import com.hamsterscreate.compat.kinetics.HamsterWheelKineticBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.hamsters.client.renderer.HamsterWheelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Mixin(GeoBlockRenderer.class)
public abstract class GeoBlockRendererMixin {
    @Inject(
        method = "render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hamsterscreatecompat$renderKineticWheel(
        BlockEntity blockEntity,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        int packedLight,
        int packedOverlay,
        CallbackInfo ci
    ) {
        if ((Object) this instanceof HamsterWheelKineticRenderer) {
            return;
        }
        if (!((Object) this instanceof HamsterWheelRenderer)) {
            return;
        }
        if (!(blockEntity instanceof HamsterWheelKineticBlockEntity kinetic)) {
            return;
        }
        CompatClient.wheelRenderer().render(kinetic, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        ci.cancel();
    }
}
