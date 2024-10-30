package com.random.randomizer.presentation.screen.edit

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.random.randomizer.R
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.presentation.core.MainActivity
import com.random.randomizer.test_util.testStringResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class EditScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val getWheelSegmentsStreamUseCase = mockk<GetWheelSegmentsStreamUseCase>()
    val createWheelSegmentUseCase = mockk<CreateWheelSegmentUseCase>()

    init {
        coEvery { createWheelSegmentUseCase() } returns WheelSegment(1, "", "", null, null)
    }

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun editScreen_showsEmptyState_whenNoWheelSegments() {
        val viewModel = createViewModelWithSegments(emptyList())
        setEditScreen(viewModel = viewModel)

        val emptyListText = testStringResource(R.string.label_no_wheel_segments)

        composeTestRule.onNodeWithText(emptyListText)
            .assertIsDisplayed()
    }

    @Test
    fun editScreen_showsWheelSegmentList_whenSegmentsPresent() {
        val viewModel = createViewModelWithSegments(
            segments = listOf(WheelSegment(1, "fake", "", null, null))
        )
        setEditScreen(viewModel = viewModel)

        composeTestRule
            .onNodeWithText("fake")
            .assertIsDisplayed()

        val emptyListText = testStringResource(R.string.label_no_wheel_segments)

        composeTestRule
            .onNodeWithText(emptyListText)
            .assertIsNotDisplayed()
    }

    @Test
    fun editScreen_showsBottomSheet_whenAddButtonClicked() {
        val viewModel = createViewModelWithSegments(emptyList())
        setEditScreen(viewModel = viewModel)

        val addButtonContentDescription = testStringResource(R.string.cd_add_new_wheel_segment)

        composeTestRule
            .onNodeWithContentDescription(addButtonContentDescription)
            .performClick()

        // Since we return WheelSegment from CreateWheelSegmentUseCase
        // without image, the add button will be visible
        val addImageButtonContentDescription = testStringResource(R.string.cd_add_image_to_segment)

        composeTestRule
            .onNodeWithContentDescription(addImageButtonContentDescription)
            .assertIsDisplayed()
    }

    private fun setEditScreen(viewModel: EditViewModel) {
        composeTestRule.activity.setContent {
            EditScreen(
                navigateBack = {},
                viewModel = viewModel
            )
        }
    }

    private fun createViewModelWithSegments(segments: List<WheelSegment>): EditViewModel {
        every { getWheelSegmentsStreamUseCase() } returns flow { emit(segments) }
        return EditViewModel(
            getWheelSegmentsStreamUseCase = getWheelSegmentsStreamUseCase,
            createWheelSegmentUseCase = createWheelSegmentUseCase,
            deleteWheelSegmentUseCase = mockk(),
            validateWheelSegmentUseCase = mockk(),
            makeWheelSegmentUniqueUseCase = mockk()
        )
    }
}