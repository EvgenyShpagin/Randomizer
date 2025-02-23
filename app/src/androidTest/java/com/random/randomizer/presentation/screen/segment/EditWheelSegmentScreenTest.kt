package com.random.randomizer.presentation.screen.segment

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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
class EditWheelSegmentScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repository: WheelSegmentRepository

    private lateinit var viewModel: EditWheelSegmentViewModel

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

    private fun setContent(wheelSegmentId: Int? = savedWheelSegment.id) {
        composeTestRule.setContent {
            viewModel = EditWheelSegmentViewModel(
                savedStateHandle = SavedStateHandle(mapOf("wheelSegmentId" to wheelSegmentId)),
                GetWheelSegmentUseCase(repository),
                FixWheelSegmentUseCase(),
                CreateWheelSegmentUseCase(FixThumbnailUseCase(), repository),
                ValidateWheelSegmentUseCase(repository),
                UpdateWheelSegmentUseCase(
                    FixThumbnailUseCase(),
                    repository,
                    ImageScalerImpl()
                ),
                EditWheelSegmentMappersImpl(CoreMappersImpl())
            )
            EditWheelSegmentScreen(
                viewModel = viewModel,
                navigateBack = {}
            )
        }
    }
}