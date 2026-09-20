package com.hamsterscreate.compat.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WheelInteractionTest {
    @Test
    fun `insert only from a hamster item into an empty wheel`() {
        assertTrue(WheelInteraction.canInsertFromHand(true, false))
        assertFalse(WheelInteraction.canInsertFromHand(true, true))
        assertFalse(WheelInteraction.canInsertFromHand(false, false))
    }

    @Test
    fun `extract only with an empty hand from an occupied wheel`() {
        assertTrue(WheelInteraction.canExtractToHand(true, true))
        assertFalse(WheelInteraction.canExtractToHand(true, false))
        assertFalse(WheelInteraction.canExtractToHand(false, true))
    }

    @Test
    fun `compat default prevents hamsters from leaving`() {
        assertTrue(WheelInteraction.shouldPreventSelfExit(false))
        assertFalse(WheelInteraction.shouldPreventSelfExit(true))
    }

    @Test
    fun `self-exit lock refreshes running ticks`() {
        assertEquals(20, WheelInteraction.keepRunningTicks(false, true, 0))
        assertEquals(20, WheelInteraction.keepRunningTicks(false, true, 1))
        assertEquals(40, WheelInteraction.keepRunningTicks(false, true, 40))
        assertEquals(0, WheelInteraction.keepRunningTicks(true, true, 0))
        assertEquals(0, WheelInteraction.keepRunningTicks(false, false, 0))
    }
}
