package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.filters.MediumTest
import com.random.randomizer.HiltTestActivity
import com.random.randomizer.R
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.test_util.testStringResource
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
    }

    @Test
    fun editScreen_showsEmptyState_whenNoWheelSegments() {
        // Given - empty wheel segment list

        // When - on startup
        setContent()

        // Then - verify label is displayed
        val emptyListText = testStringResource(R.string.label_no_wheel_segments)
        composeTestRule.onNodeWithText(emptyListText)
            .assertIsDisplayed()
    }

    @Test
    fun editScreen_showsWheelSegmentList_whenSegmentsPresent() = runTest {
        // Given - single wheel segment
        wheelSegmentRepository.add(WheelSegment(1, "fake", "", null, null))

        // When - on startup
        setContent()

        // Then - verify wheel segment is displayed
        composeTestRule
            .onNodeWithText("fake")
            .assertIsDisplayed()

        // and also - that empty label is not displayed
        val emptyListText = testStringResource(R.string.label_no_wheel_segments)
        composeTestRule
            .onNodeWithText(emptyListText)
            .assertIsNotDisplayed()
    }

    @Test
    fun editScreen_showsBottomSheet_whenAddButtonClicked() {
        // Given - empty wheel segment list

        setContent()

        // When - on add button clicked
        val addButtonContentDescription = testStringResource(R.string.cd_add_new_wheel_segment)
        composeTestRule
            .onNodeWithContentDescription(addButtonContentDescription)
            .performClick()

        // Then - verify some bottom sheet content is displayed
        val addImageButtonContentDescription = testStringResource(R.string.cd_add_image_to_segment)
        // Since we return WheelSegment from CreateWheelSegmentUseCase
        // without image, the add button will be visible
        composeTestRule
            .onNodeWithContentDescription(addImageButtonContentDescription)
            .assertIsDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            viewModel = hiltViewModel()
            EditScreen(
                navigateBack = {},
                viewModel = viewModel
            )
        }
    }
}