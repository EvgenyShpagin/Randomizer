package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.filters.MediumTest
import com.random.randomizer.HiltTestActivity
import com.random.randomizer.R
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
class EditScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var viewModel: EditViewModel

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

        // Then - verify label is displayed
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

        // and also - that empty label is not displayed
        val emptyListText = stringResource(R.string.label_no_wheel_segments)
        composeTestRule
            .onNodeWithText(emptyListText)
            .assertIsNotDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            viewModel = hiltViewModel()
            EditScreen(
                navigateBack = {},
                navigateToEditDetails = {},
                viewModel = viewModel
            )
        }
    }
}