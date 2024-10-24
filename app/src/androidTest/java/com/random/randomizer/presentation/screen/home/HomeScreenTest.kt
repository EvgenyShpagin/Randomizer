package com.random.randomizer.presentation.screen.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.random.randomizer.R
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.test_util.testStringResource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_showsEmptyState_whenNoWheelSegments() {
        val emptyListViewModel = getMockViewModelWithState(
            HomeUiState(wheelSegments = emptyList())
        )
        setHomeScreen(emptyListViewModel)

        val emptyListText = testStringResource(R.string.label_no_wheel_segments)

        composeTestRule.onNodeWithText(emptyListText)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsWheelSegmentList_whenSegmentsPresent() {
        val nonEmptyListViewModel = getMockViewModelWithState(
            HomeUiState(wheelSegments = listOf(WheelSegmentUiState(1, "fake")))
        )
        setHomeScreen(nonEmptyListViewModel)

        composeTestRule
            .onNodeWithText("fake")
            .assertIsDisplayed()

        val emptyListText = testStringResource(R.string.label_no_wheel_segments)

        composeTestRule
            .onNodeWithText(emptyListText)
            .assertIsNotDisplayed()
    }

    @Test
    fun homeScreen_hidesSpinButton_whenNoWheelSegments() {
        val emptyListViewModel = getMockViewModelWithState(
            HomeUiState(wheelSegments = emptyList())
        )
        setHomeScreen(emptyListViewModel)

        val spinButtonText = testStringResource(R.string.button_spin)
        composeTestRule
            .onNodeWithText(spinButtonText, useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    @Test
    fun homeScreen_showsSpinButton_whenWheelSegmentsPresent() {
        val nonEmptyListViewModel = getMockViewModelWithState(
            HomeUiState(wheelSegments = listOf(WheelSegmentUiState(1, "fake")))
        )
        setHomeScreen(nonEmptyListViewModel)

        val spinButtonText = testStringResource(R.string.button_spin)
        composeTestRule
            .onNodeWithText(spinButtonText, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    private fun setHomeScreen(viewModel: HomeViewModel) {
        composeTestRule.setContent {
            HomeScreen(
                navigateToSpin = {},
                navigateToEdit = {},
                viewModel = viewModel
            )
        }
    }

    private fun getMockViewModelWithState(uiState: HomeUiState): HomeViewModel {
        return mockk<HomeViewModel>().also { viewModel ->
            every { viewModel.uiState } returns MutableStateFlow(uiState)
        }
    }
}