package com.random.randomizer.presentation.core

import androidx.compose.ui.graphics.Color
import com.random.randomizer.domain.model.WheelSegment
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MappersTest {

    val coreMappers = CoreMappersImpl()

    @Test
    fun presentationColor_to_domainColor_mapsCorrectly() {
        val expectedDomainColor = 0xFFFF0000
        val actualPresentationColorLong = coreMappers.toDomain(Color.Red)
        assertEquals(expectedDomainColor, actualPresentationColorLong)
    }

    @Test
    fun domainColor_to_presentationColor_mapsCorrectly() {
        val expectedPresentationColor = Color.Red
        val actualPresentationColor = Color(0xFFFF0000)
        assertEquals(expectedPresentationColor, actualPresentationColor)
    }

    @Test
    fun domainWheelSegment_to_presentationWheelSegmentUiState_mapsCorrectly() {
        val domain = WheelSegment(
            id = 5,
            title = "Some title",
            description = "Some description",
            thumbnailPath = null,
            customColor = 0x80FAFAFA
        )

        val presentationUiState = coreMappers.toPresentation(domain)

        assertEquals(domain.id, presentationUiState.id)
        assertEquals(domain.title, presentationUiState.title)
        assertEquals(domain.description, presentationUiState.description)
        assertEquals(null, presentationUiState.image)
        assertEquals(
            domain.customColor,
            presentationUiState.customColor?.value?.shr(32)?.toLong()
        )
    }

    @Test
    fun presentationWheelSegmentUiState_to_domainWheelSegment_mapsCorrectly() {
        val presentationUiState = WheelSegmentUiState(
            id = 5,
            title = "Some title",
            description = "Some description",
            image = null,
            customColor = Color.Red
        )

        val domain = coreMappers.toDomain(presentationUiState, null)

        assertEquals(presentationUiState.id, domain.id)
        assertEquals(presentationUiState.title, domain.title)
        assertEquals(presentationUiState.description, domain.description)
        assertEquals(presentationUiState.image, null)
        assertEquals(
            presentationUiState.customColor?.let { coreMappers.toDomain(it) },
            domain.customColor
        )
    }
}