package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.MediumTest
import com.random.randomizer.HiltTestActivity
import com.random.randomizer.R
import com.random.randomizer.data.util.ImageScalerImpl
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.FixThumbnailUseCase
import com.random.randomizer.domain.usecase.FixWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentUseCase
import com.random.randomizer.domain.usecase.UpdateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.ValidateWheelSegmentUseCase
import com.random.randomizer.presentation.core.CoreMappersImpl
import com.random.randomizer.test_util.stringResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
class EditScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repository: WheelSegmentRepository

    private lateinit var viewModel: EditViewModel

    private val savedWheelSegment = WheelSegment(
        id = 1,
        title = "Saved wheel segment title",
        description = "Saved wheel segment description",
        thumbnail = null,
        customColor = 0xFFBB86FC
    )

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun displaysEmptyWheelSegment_whenJustCreated() = runTest {
        // Given - just created wheel segment
        // When - on startup
        setContent(wheelSegmentId = null)

        // Then - verify blank wheel segment is displayed
        composeTestRule.onAllNodes(hasSetTextAction())
            .assertAll(hasText(""))
        composeTestRule
            .onNodeWithContentDescription(stringResource(R.string.cd_remove_color))
            .assertIsNotDisplayed()
    }

    @Test
    fun displaysExistingWheelSegment_whenOpenedAlreadySaved() = runTest {
        // Given - saved wheel segment
        repository.add(savedWheelSegment)

        // When - on startup
        setContent(wheelSegmentId = savedWheelSegment.id)

        // Then - verify content is displayed
        composeTestRule
            .onAllNodesWithText(savedWheelSegment.title)
            .apply {
                onFirst().assertIsDisplayed() // In preview
                onLast().assertIsDisplayed() // In text field
            }
        composeTestRule
            .onAllNodesWithText(savedWheelSegment.description)
            .apply {
                onFirst().assertIsDisplayed() // In preview
                onLast().assertIsDisplayed() // In text field
            }
        composeTestRule
            .onNodeWithContentDescription(stringResource(R.string.cd_remove_color))
            .assertIsDisplayed()
    }

    @Test
    fun updatesTitle_onInput() = runTest {
        // Given - saved wheel segment
        repository.add(savedWheelSegment)

        setContent()

        // When - on title input
        composeTestRule.onNode(
            hasSetTextAction() and hasText(savedWheelSegment.title)
        ).performTextReplacement("Some other title text")

        // Then - verify title has changed
        assertEquals(
            "Some other title text",
            viewModel.uiState.value.segmentUiState.title
        )
    }

    @Test
    fun updatesDescription_onInput() = runTest {
        // Given - saved wheel segment
        repository.add(savedWheelSegment)

        setContent()

        // When - on description input
        composeTestRule.onNode(
            hasSetTextAction() and hasText(savedWheelSegment.description)
        ).performTextReplacement("Some other description")

        // Then - verify description has changed
        assertEquals(
            "Some other description",
            viewModel.uiState.value.segmentUiState.description
        )
    }

    @Test
    fun updatesColor_onPick() = runTest {
        // Given - saved wheel segment
        repository.add(savedWheelSegment)

        setContent()

        val color = AllSegmentColors[0]

        // When - on color pick
        composeTestRule.onNodeWithContentDescription(
            stringResource(R.string.cd_unchecked_color_circle, color.toString())
        ).performClick()

        // Then - verify color has changed
        assertEquals(color, viewModel.uiState.value.segmentUiState.customColor)
    }

    @Test
    fun keepsState_whenActivityRecreated() = runTest {
        // Given - saved wheel segment
        repository.add(savedWheelSegment)

        setContent()

        // Input title
        composeTestRule.onNode(
            hasSetTextAction() and hasText(savedWheelSegment.title)
        ).performTextReplacement("Some other title text")

        // Input description
        composeTestRule.onNode(
            hasSetTextAction() and hasText(savedWheelSegment.description)
        ).performTextReplacement("Some other description")

        val color = AllSegmentColors[0]
        composeTestRule.onNodeWithContentDescription(
            stringResource(R.string.cd_unchecked_color_circle, color.toString())
        ).performClick()

        // When - activity was recreated
        composeTestRule.activityRule.scenario.recreate()

        // Then - verify title has changed
        assertEquals(
            "Some other title text",
            viewModel.uiState.value.segmentUiState.title
        )
        // Then - verify description has changed
        assertEquals(
            "Some other description",
            viewModel.uiState.value.segmentUiState.description
        )
        // Then - verify color has changed
        assertEquals(color, viewModel.uiState.value.segmentUiState.customColor)
    }

    @Test
    fun updatesWheelSegment_whenFinishWithSave() = runTest {
        // Given - saved wheel segment
        repository.add(savedWheelSegment)

        setContent()

        composeTestRule.onNode(
            hasSetTextAction() and hasText(savedWheelSegment.title)
        ).performTextReplacement("Some other title text")

        // When - on save button click
        composeTestRule
            .onNodeWithText(stringResource(R.string.button_save))
            .performClick()

        // Then - verify wheel segment has been saved
        assertEquals(
            savedWheelSegment.copy(title = "Some other title text"),
            repository.get(savedWheelSegment.id)
        )
    }

    @Test
    fun doesNotUpdateWheelSegment_whenFinishWithoutSave() = runTest {
        // Given - saved wheel segment
        repository.add(savedWheelSegment)

        setContent()

        composeTestRule.onNode(
            hasSetTextAction() and hasText(savedWheelSegment.title)
        ).performTextReplacement("Some other title text")

        // When - back button is clicked
        composeTestRule.runOnUiThread {
            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        // Then - verify wheel segment has not been updated
        assertEquals(
            savedWheelSegment,
            repository.get(savedWheelSegment.id)
        )
    }

    private fun setContent(wheelSegmentId: Int? = savedWheelSegment.id) {
        composeTestRule.setContent {
            viewModel = EditViewModel(
                LocalContext.current,
                SavedStateHandle(mapOf("wheelSegmentId" to wheelSegmentId)),
                GetWheelSegmentUseCase(repository),
                FixWheelSegmentUseCase(),
                CreateWheelSegmentUseCase(FixThumbnailUseCase(), repository),
                ValidateWheelSegmentUseCase(repository),
                UpdateWheelSegmentUseCase(
                    FixThumbnailUseCase(),
                    repository,
                    ImageScalerImpl()
                )
            )
            EditScreen(
                viewModel = viewModel,
                navigateBack = {}
            )
        }
    }
}