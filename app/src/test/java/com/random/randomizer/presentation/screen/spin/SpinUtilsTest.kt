package com.random.randomizer.presentation.screen.spin

import com.random.randomizer.presentation.core.WheelSegmentUiState
import junit.framework.TestCase.assertEquals
import org.junit.Test

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
}