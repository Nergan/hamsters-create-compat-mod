package com.hamsterscreate.compat.logic

/**
 * Create hand-crank defaults (8 SU/RPM at 32 RPM = 256 SU).
 * The hamster wheel animation spins opposite Create's positive axis speed
 * after FACING is applied, so [WHEEL_SPIN_SIGN] is inverted.
 */
object WheelKinetics {
    const val HAND_CRANK_RPM: Int = 32
    const val HAND_CRANK_SU_PER_RPM: Double = 8.0
    const val WHEEL_SPIN_SIGN: Int = -1

    @JvmStatic
    fun rotationAxis(facing: CompatDirection): CompatAxis = facing.axis

    @JvmStatic
    fun hasShaftTowards(facing: CompatDirection, face: CompatDirection): Boolean =
        facing.axis == face.axis

    @JvmStatic
    fun generatedRpm(occupied: Boolean, configuredRpm: Int): Int =
        if (occupied && configuredRpm != 0) configuredRpm else 0

    @JvmStatic
    fun convertToDirection(axisSpeed: Float, facing: CompatDirection): Float =
        if (facing.positive) axisSpeed else -axisSpeed

    @JvmStatic
    fun generatedSpeed(occupied: Boolean, configuredRpm: Int, facing: CompatDirection): Float {
        val rpm = generatedRpm(occupied, configuredRpm)
        if (rpm == 0) {
            return 0f
        }
        return convertToDirection(rpm.toFloat() * WHEEL_SPIN_SIGN, facing)
    }

    @JvmStatic
    fun totalStressUnits(rpm: Int, suPerRpm: Double): Double =
        kotlin.math.abs(rpm) * suPerRpm
}
