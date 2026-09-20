package com.hamsterscreate.compat.kinetics

import com.hamsterscreate.compat.config.CompatServerConfig
import com.simibubi.create.api.stress.BlockStressValues
import net.minecraft.world.level.block.Block

object HamsterWheelStress {
    @JvmStatic
    fun register(wheel: Block) {
        BlockStressValues.CAPACITIES.register(wheel) {
            CompatServerConfig.stressCapacityPerRpm()
        }
        BlockStressValues.RPM.register(
            wheel,
            BlockStressValues.GeneratedRpm(CompatServerConfig.generatedRpm(), true)
        )
    }
}
