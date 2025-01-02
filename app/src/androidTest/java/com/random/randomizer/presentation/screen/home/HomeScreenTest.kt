package com.random.randomizer.presentation.screen.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.filters.MediumTest
import com.random.randomizer.R
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.test_util.testStringResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    val getWheelSegmentsStreamUseCase = mockk<GetWheelSegmentsStreamUseCase>()

    @Inject
    lateinit var mappers: HomeMappers

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun homeScreen_showsEmptyState_whenNoWheelSegments() {
        val viewModel = createViewModelWithSegments(emptyList())
        setHomeScreen(viewModel = viewModel)

        val emptyListText = testStringResource(R.string.label_no_wheel_segments)

        composeTestRule.onNodeWithText(emptyListText)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsWheelSegmentList_whenSegmentsPresent() {
        val viewModel = createViewModelWithSegments(
            segments = listOf(WheelSegment(1, "fake", "", null, null))
        )
        setHomeScreen(viewModel = viewModel)

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
        val viewModel = createViewModelWithSegments(emptyList())
        setHomeScreen(viewModel = viewModel)

        val spinButtonText = testStringResource(R.string.button_spin)
        composeTestRule
            .onNodeWithText(spinButtonText, useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    @Test
    fun homeScreen_hidesSpinButton_whenSingleWheelSegment() {
        val viewModel = createViewModelWithSegments(
            segments = listOf(WheelSegment(1, "fake", "", null, null))
        )
        setHomeScreen(viewModel = viewModel)

        val spinButtonText = testStringResource(R.string.button_spin)
        composeTestRule
            .onNodeWithText(spinButtonText, useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    @Test
    fun homeScreen_showsSpinButton_whenWheelSegmentsPresent() {
        val viewModel = createViewModelWithSegments(
            segments = listOf(
                WheelSegment(1, "fake", "", null, null),
                WheelSegment(2, "fake2", "", null, null)
            )
        )
        setHomeScreen(viewModel = viewModel)

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

    private fun createViewModelWithSegments(segments: List<WheelSegment>): HomeViewModel {
        every { getWheelSegmentsStreamUseCase() } returns flow { emit(segments) }
        return HomeViewModel(getWheelSegmentsStreamUseCase, mappers)
    }
}