package com.hamsterscreate.compat.kinetics

import com.hamsterscreate.compat.logic.CompatDirection
import net.minecraft.core.Direction

fun Direction.toCompat(): CompatDirection = CompatDirection.fromName(this.name)

object DirectionMapping {
    @JvmStatic
    fun toCompat(direction: Direction): CompatDirection = direction.toCompat()
}
