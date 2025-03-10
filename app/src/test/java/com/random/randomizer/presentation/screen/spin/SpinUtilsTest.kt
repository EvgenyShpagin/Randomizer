package com.random.randomizer.presentation.screen.spin

import com.random.randomizer.presentation.core.WheelSegmentUiState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.math.roundToInt

class SpinUtilsTest {

    @Test
    fun extendTo_doublesList_whenDoubleExtended() {
        val originSegments = List(5) { i ->
            WheelSegmentUiState(i, "fake$i", "", null, null)
        }
        val extendedSegments = originSegments.extendTo(10)
        assertEquals(10, extendedSegments.count())
    }

    @Test
    fun extendTo_preservesOriginalItems_whenExtended() {
        val originSegments = List(6) { i ->
            WheelSegmentUiState(i, "fake$i", "", null, null)
        }
        val extendedSegments = originSegments.extendTo(12)

        assertEquals(originSegments, extendedSegments.subList(0, 6))
        assertEquals(originSegments, extendedSegments.subList(6, 12))
    }

    @Test
    fun extendTo_roundsUpSize_whenSizeIsNotPerfectMultiple() {
        val originSegments = List(7) { i ->
            WheelSegmentUiState(i, "fake$i", "", null, null)
        }

        val extendedSegments = originSegments.extendTo(8)

        assertEquals(14, extendedSegments.count())
    }

    @Test
    fun extendTo_keepsSameList_whenMinCountLessThanOriginalSize() {
        val originSegments = List(8) { i ->
            WheelSegmentUiState(i, "fake$i", "", null, null)
        }

        val extendedSegments = originSegments.extendTo(2)

        assertEquals(8, extendedSegments.count())
    }

    @Test
    fun getSpinDurationMillis_returnsMaxValue_whenTargetIndexIsVeryLarge() {
        val targetIndex = 500
        val avgSegmentSpinTime = 110
        val scatter = 0.2
        val maxValue = 12_000

        val result = getSpinDurationMillis(targetIndex, avgSegmentSpinTime, scatter, maxValue)

        assertTrue(
            "Expected maxValue $maxValue but got $result",
            result == maxValue
        )
    }

    @Test
    fun getSpinDurationMillis_generatesValuesWithinRange_whenScatterIsApplied() {
        val targetIndex = 10
        val avgSegmentSpinTime = 100
        val scatter = 0.3
        val maxValue = 12_000

        val results = List(100) {
            getSpinDurationMillis(targetIndex, avgSegmentSpinTime, scatter, maxValue)
        }

        val minExpected = (targetIndex * avgSegmentSpinTime * (1 - scatter)).roundToInt()
        val maxExpected = (targetIndex * avgSegmentSpinTime * (1 + scatter)).roundToInt()

        assertTrue(
            "Some values are out of range [$minExpected, $maxExpected]",
            results.all { it in minExpected..maxExpected }
        )
    }
}