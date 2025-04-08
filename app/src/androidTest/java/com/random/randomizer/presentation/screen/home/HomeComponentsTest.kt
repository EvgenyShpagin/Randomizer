package com.random.randomizer.presentation.screen.home

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.random.randomizer.R
import com.random.randomizer.test_util.stringResource
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class HomeComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun fabsColumn_showsOnlyAddButton_onOnlyAddButtonState() = runTest {
        composeTestRule.setContent {
            FabsColumn(
                state = FabsColumnState.OnlyAddButton,
                onClickAdd = {},
                onClickSpin = {}
            )
        }

        composeTestRule.onAllNodes(hasClickAction())
            .assertCountEquals(expectedSize = 1)
    }

    @Test
    fun fabsColumn_ignoresSpinClick_onOnlyAddButtonState() = runTest {
        composeTestRule.setContent {
            FabsColumn(
                state = FabsColumnState.OnlyAddButton,
                onClickAdd = {},
                onClickSpin = { throw IllegalStateException() }
            )
        }

        composeTestRule.onNodeWithText(stringResource(R.string.button_spin), useUnmergedTree = true)
            .takeIf { it.isDisplayed() }
            ?.performClick()
    }

    @Test
    fun fabsColumn_showsAddAndSpinButtons_onAddAndSpinButtonsState() = runTest {
        composeTestRule.setContent {
            FabsColumn(
                state = FabsColumnState.AddAndSpinButton,
                onClickAdd = {},
                onClickSpin = {}
            )
        }

        composeTestRule.onAllNodes(hasClickAction())
            .assertCountEquals(expectedSize = 2)
    }
}