package com.hamsterscreate.compat.client

import com.hamsterscreate.compat.kinetics.HamsterWheelKineticBlockEntity
import com.starfish_studios.hamsters.Hamsters
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.DefaultedBlockGeoModel

class HamsterWheelKineticModel : DefaultedBlockGeoModel<HamsterWheelKineticBlockEntity>(
    ResourceLocation(Hamsters.MOD_ID, "hamster_wheel")
) {
    override fun getAnimationResource(animatable: HamsterWheelKineticBlockEntity): ResourceLocation {
        return ResourceLocation(Hamsters.MOD_ID, "animations/hamster_wheel.animation.json")
    }

    override fun getRenderType(animatable: HamsterWheelKineticBlockEntity, texture: ResourceLocation): RenderType {
        return RenderType.entityCutout(getTextureResource(animatable))
    }
}
