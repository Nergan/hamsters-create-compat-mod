package com.hamsterscreate.compat.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WheelKineticsTest {
    @Test
    fun `rotation axis follows wheel facing`() {
        assertEquals(CompatAxis.Z, WheelKinetics.rotationAxis(CompatDirection.NORTH))
        assertEquals(CompatAxis.Z, WheelKinetics.rotationAxis(CompatDirection.SOUTH))
        assertEquals(CompatAxis.X, WheelKinetics.rotationAxis(CompatDirection.EAST))
        assertEquals(CompatAxis.X, WheelKinetics.rotationAxis(CompatDirection.WEST))
    }

    @Test
    fun `shaft connects only on the spoke hub face`() {
        assertFalse(WheelKinetics.hasShaftTowards(CompatDirection.NORTH, CompatDirection.NORTH))
        assertTrue(WheelKinetics.hasShaftTowards(CompatDirection.NORTH, CompatDirection.SOUTH))
        assertFalse(WheelKinetics.hasShaftTowards(CompatDirection.NORTH, CompatDirection.EAST))
        assertTrue(WheelKinetics.hasShaftTowards(CompatDirection.WEST, CompatDirection.EAST))
        assertFalse(WheelKinetics.hasShaftTowards(CompatDirection.WEST, CompatDirection.WEST))
        assertFalse(WheelKinetics.hasShaftTowards(CompatDirection.WEST, CompatDirection.UP))
        assertTrue(WheelKinetics.hasShaftTowards(CompatDirection.SOUTH, CompatDirection.NORTH))
        assertFalse(WheelKinetics.hasShaftTowards(CompatDirection.SOUTH, CompatDirection.SOUTH))
        assertTrue(WheelKinetics.hasShaftTowards(CompatDirection.EAST, CompatDirection.WEST))
        assertFalse(WheelKinetics.hasShaftTowards(CompatDirection.EAST, CompatDirection.EAST))
    }

    @Test
    fun `empty wheel produces no speed`() {
        assertEquals(0f, WheelKinetics.generatedSpeed(false, 32, CompatDirection.SOUTH))
    }

    @Test
    fun `occupied wheel matches hand crank rpm and create facing sign`() {
        assertEquals(-32f, WheelKinetics.generatedSpeed(true, WheelKinetics.HAND_CRANK_RPM, CompatDirection.SOUTH))
        assertEquals(32f, WheelKinetics.generatedSpeed(true, WheelKinetics.HAND_CRANK_RPM, CompatDirection.NORTH))
        assertEquals(-32f, WheelKinetics.generatedSpeed(true, WheelKinetics.HAND_CRANK_RPM, CompatDirection.EAST))
        assertEquals(32f, WheelKinetics.generatedSpeed(true, WheelKinetics.HAND_CRANK_RPM, CompatDirection.WEST))
    }

    @Test
    fun `opposite facings reverse the shaft`() {
        val south = WheelKinetics.generatedSpeed(true, 32, CompatDirection.SOUTH)
        val north = WheelKinetics.generatedSpeed(true, 32, CompatDirection.NORTH)
        assertEquals(-south, north)
    }

    @Test
    fun `default energy matches create hand crank`() {
        assertEquals(
            256.0,
            WheelKinetics.totalStressUnits(WheelKinetics.HAND_CRANK_RPM, WheelKinetics.HAND_CRANK_SU_PER_RPM)
        )
    }

    @Test
    fun `configured stress scales with rpm`() {
        assertEquals(128.0, WheelKinetics.totalStressUnits(16, 8.0))
        assertEquals(0.0, WheelKinetics.generatedRpm(false, 32).toDouble())
    }

    @Test
    fun `variant scale multiplies configured rpm`() {
        assertEquals(2, WheelKinetics.variantScale("black"))
        assertEquals(1, WheelKinetics.variantScale("BLACK_WHITE"))
        assertEquals(1, WheelKinetics.variantScale("orange"))
        assertEquals(
            -64f,
            WheelKinetics.generatedSpeed(
                true,
                WheelKinetics.HAND_CRANK_RPM * WheelKinetics.variantScale("black"),
                CompatDirection.SOUTH
            )
        )
        assertEquals(
            512.0,
            WheelKinetics.totalStressUnits(
                WheelKinetics.HAND_CRANK_RPM * WheelKinetics.variantScale("black"),
                WheelKinetics.HAND_CRANK_SU_PER_RPM
            )
        )
    }
}
