package com.random.randomizer.presentation.screen.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.filters.MediumTest
import com.random.randomizer.HiltTestActivity
import com.random.randomizer.R
import com.random.randomizer.data.addMultiple
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.test_util.stringResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
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
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var wheelSegmentRepository: WheelSegmentRepository

    @Before
    fun setup() {
        hiltRule.inject()
        setContent()
    }

    @Test
    fun showsEmptyState_whenNoWheelSegments() {
        // Given - empty wheel segment list

        // When - on startup

        // Then - verify empty label is displayed
        val emptyListText = stringResource(R.string.label_no_wheel_segments)
        composeTestRule.onNodeWithText(emptyListText)
            .assertIsDisplayed()
    }

    @Test
    fun showsWheelSegmentList_whenSegmentsPresent() = runTest {
        // Given - single wheel segment
        wheelSegmentRepository.add(WheelSegment(1, "fake", "", null, null))

        // When - on startup

        // Then - verify wheel segment is displayed
        composeTestRule
            .onNodeWithText("fake")
            .assertIsDisplayed()

        // and empty label is not displayed
        val emptyListText = stringResource(R.string.label_no_wheel_segments)
        composeTestRule
            .onNodeWithText(emptyListText)
            .assertIsNotDisplayed()
    }

    @Test
    fun hidesSpinButton_whenNoWheelSegments() {
        // Given - empty wheel segment list

        // When - on startup

        // Then - verify button is hidden
        val spinButtonText = stringResource(R.string.button_spin)
        composeTestRule
            .onNodeWithText(spinButtonText, useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    @Test
    fun hidesSpinButton_whenSingleWheelSegment() = runTest {
        // Given - single wheel segment
        wheelSegmentRepository.add(WheelSegment(1, "fake", "", null, null))

        // When - on startup

        // Then - verify button is hidden
        val spinButtonText = stringResource(R.string.button_spin)
        composeTestRule
            .onNodeWithText(spinButtonText, useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    @Test
    fun showsSpinButton_whenWheelSegmentsPresent() = runTest {
        // Given - single wheel segment
        wheelSegmentRepository.addMultiple(
            segments = listOf(
                WheelSegment(1, "fake", "", null, null),
                WheelSegment(2, "fake2", "", null, null)
            )
        )

        // When - on startup

        // Then - verify button is displayed
        val spinButtonText = stringResource(R.string.button_spin)
        composeTestRule
            .onNodeWithText(spinButtonText, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            viewModel = hiltViewModel()
            HomeScreen(
                navigateToSpin = {},
                navigateToEdit = {},
                viewModel = viewModel
            )
        }
    }
}