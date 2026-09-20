package com.hamsterscreate.compat.logic

/**
 * Loader-agnostic facing used by tests and by the 1.20.1 / future 1.21.1 adapters.
 * Names match vanilla Direction.
 */
enum class CompatAxis {
    X, Y, Z
}

enum class CompatDirection(val axis: CompatAxis, val positive: Boolean) {
    DOWN(CompatAxis.Y, false),
    UP(CompatAxis.Y, true),
    NORTH(CompatAxis.Z, false),
    SOUTH(CompatAxis.Z, true),
    WEST(CompatAxis.X, false),
    EAST(CompatAxis.X, true);

    companion object {
        @JvmStatic
        fun fromName(name: String): CompatDirection = valueOf(name.uppercase())
    }
}
