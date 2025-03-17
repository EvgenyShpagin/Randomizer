package com.random.randomizer.presentation.screen.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.filters.MediumTest
import com.random.randomizer.HiltTestActivity
import com.random.randomizer.R
import com.random.randomizer.data.repository.addMultiple
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.test_util.setContentWithSharedTransition
import com.random.randomizer.test_util.stringResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalSharedTransitionApi::class)
@MediumTest
@HiltAndroidTest
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var repository: WheelSegmentRepository

    private val wheelSegments = listOf(
        WheelSegment(1, "fake", "", null, null),
        WheelSegment(2, "fake2", "", null, null)
    )

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
        repository.add(wheelSegments.first())

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
        repository.add(wheelSegments.first())

        // When - on startup

        // Then - verify button is hidden
        val spinButtonText = stringResource(R.string.button_spin)
        composeTestRule
            .onNodeWithText(spinButtonText, useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    @Test
    fun showsSpinButton_whenWheelSegmentsPresent() = runTest {
        // Given - multiple wheel segments
        repository.addMultiple(wheelSegments)

        // When - on startup

        // Then - verify button is displayed
        val spinButtonText = stringResource(R.string.button_spin)
        composeTestRule
            .onNodeWithText(spinButtonText, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun deletesWheelSegment_whenWheelSegmentSwiped() = runTest {
        // Given - multiple wheel segments
        repository.addMultiple(wheelSegments)

        // When - item is swiped
        composeTestRule
            .onNodeWithText(wheelSegments.first().title)
            .performTouchInput { swipeLeft() }

        composeTestRule.waitForIdle()

        // Then - verify item has been deleted
        val resultWheelSegments = repository.getAll()
        assertEquals(
            listOf(wheelSegments.last()),
            resultWheelSegments
        )
    }

    private fun setContent() {
        composeTestRule.setContentWithSharedTransition { animatedVisibilityScope ->
            viewModel = hiltViewModel()
            HomeScreen(
                animatedVisibilityScope = animatedVisibilityScope,
                navigateToSpin = {},
                navigateToEdit = {},
                viewModel = viewModel
            )
        }
    }
}