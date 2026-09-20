package com.hamsterscreate.compat.logic

object WheelInteraction {
    @JvmStatic
    fun canInsertFromHand(holdingHamster: Boolean, wheelOccupied: Boolean): Boolean =
        holdingHamster && !wheelOccupied

    @JvmStatic
    fun canExtractToHand(wheelOccupied: Boolean, emptyHand: Boolean): Boolean =
        wheelOccupied && emptyHand

    @JvmStatic
    fun shouldPreventSelfExit(configAllowsSelfExit: Boolean): Boolean = !configAllowsSelfExit

    @JvmStatic
    fun keepRunningTicks(configAllowsSelfExit: Boolean, isSeatedInWheel: Boolean, currentTicks: Int): Int {
        if (configAllowsSelfExit || !isSeatedInWheel) {
            return currentTicks
        }
        return if (currentTicks <= 1) 20 else currentTicks
    }
}
