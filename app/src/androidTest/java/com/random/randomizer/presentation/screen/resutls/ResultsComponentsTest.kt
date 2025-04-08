package com.random.randomizer.presentation.screen.resutls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.random.randomizer.R
import com.random.randomizer.presentation.screen.results.SpinButtons
import com.random.randomizer.test_util.stringResource
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test

class ResultsComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun spinButtons_showsOnlySpinButton() {
        composeTestRule.setContent {
            SpinButtons(
                onSpinClicked = {},
                onDeleteAndSpinClicked = {},
                showDeleteButton = false
            )
        }

        composeTestRule
            .onNodeWithText(stringResource(R.string.button_delete_and_spin))
            .assertIsNotDisplayed()
    }

    @Test
    fun spinButtons_showsBothButtons() {
        composeTestRule.setContent {
            SpinButtons(
                onSpinClicked = {},
                onDeleteAndSpinClicked = {},
                showDeleteButton = true
            )
        }

        composeTestRule
            .onNodeWithText(stringResource(R.string.button_delete_and_spin))
            .assertIsDisplayed()
    }

    @Test
    fun spinButtons_placedVertically_whenShowBothAndWidthIsNotEnough() {
        composeTestRule.setContent {
            Box(modifier = Modifier.width(330.dp - 1.dp)) {
                SpinButtons(
                    onSpinClicked = {},
                    onDeleteAndSpinClicked = {},
                    showDeleteButton = true
                )
            }
        }

        val deleteButtonPosition = composeTestRule
            .onNodeWithText(stringResource(R.string.button_delete_and_spin))
            .positionInParent()

        val spinButtonPosition = composeTestRule
            .onNodeWithText(stringResource(R.string.button_spin))
            .positionInParent()


        assertEquals(deleteButtonPosition.x, spinButtonPosition.x)

        assertNotEquals(deleteButtonPosition.y, spinButtonPosition.y)
    }

    @Test
    fun spinButtons_placedHorizontally_whenShowBothAndWidthIsEnough() {
        composeTestRule.setContent {
            Box(modifier = Modifier.width(330.dp)) {
                SpinButtons(
                    onSpinClicked = {},
                    onDeleteAndSpinClicked = {},
                    showDeleteButton = true
                )
            }
        }

        val deleteButtonPosition = composeTestRule
            .onNodeWithText(stringResource(R.string.button_delete_and_spin))
            .positionInParent()

        val spinButtonPosition = composeTestRule
            .onNodeWithText(stringResource(R.string.button_spin))
            .positionInParent()

        assertNotEquals(deleteButtonPosition.x, spinButtonPosition.x)
        assertEquals(deleteButtonPosition.y, spinButtonPosition.y)
    }

    fun SemanticsNodeInteraction.positionInParent(): Offset {
        return this
            .fetchSemanticsNode()
            .layoutInfo
            .coordinates
            .positionInParent()
    }
}