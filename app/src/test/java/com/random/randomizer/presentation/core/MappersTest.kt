package com.random.randomizer.presentation.core

import androidx.compose.ui.graphics.Color
import com.random.randomizer.domain.model.WheelSegment
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MappersTest {

    @Test
    fun toDomain_convertsColorCorrectly() {
        val expectedDomainColor = 0xFFFF0000
        val actualPresentationColorLong = toDomain(Color.Red)
        assertEquals(expectedDomainColor, actualPresentationColorLong)
    }

    @Test
    fun toPresentation_convertsColorCorrectly() {
        val expectedPresentationColor = Color.Red
        val actualPresentationColor = Color(0xFFFF0000)
        assertEquals(expectedPresentationColor, actualPresentationColor)
    }

    @Test
    fun toPresentation_converts_wheelSegmentCorrectly() {
        val domain = WheelSegment(
            id = 5,
            title = "Some title",
            description = "Some description",
            thumbnail = null,
            customColor = 0x80FAFAFA
        )

        val presentationUiState = toPresentation(domain)

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
    fun toDomain_convertsWheelSegmentUiStateCorrectly() {
        val presentationUiState = WheelSegmentUiState(
            id = 5,
            title = "Some title",
            description = "Some description",
            image = null,
            customColor = Color.Red
        )

        val domain = toDomain(presentationUiState, null)

        assertEquals(presentationUiState.id, domain.id)
        assertEquals(presentationUiState.title, domain.title)
        assertEquals(presentationUiState.description, domain.description)
        assertEquals(presentationUiState.image, null)
        assertEquals(
            presentationUiState.customColor?.let { toDomain(it) },
            domain.customColor
        )
    }
}