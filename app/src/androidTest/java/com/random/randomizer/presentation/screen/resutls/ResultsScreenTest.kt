package com.random.randomizer.presentation.screen.resutls

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.MediumTest
import com.random.randomizer.HiltTestActivity
import com.random.randomizer.R
import com.random.randomizer.data.repository.addMultiple
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentStreamUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsUseCase
import com.random.randomizer.presentation.screen.results.ResultsScreen
import com.random.randomizer.presentation.screen.results.ResultsViewModel
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
class ResultsScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repository: WheelSegmentRepository

    private lateinit var viewModel: ResultsViewModel

    private val wheelSegmentList = List(5) { i ->
        WheelSegment(
            id = i + 1, // start from 1 (0 is not valid id)
            title = "Title of $i wheel segment",
            description = "Description...",
            thumbnail = null,
            customColor = null
        )
    }

    private val wheelSegmentMinList = wheelSegmentList.take(2)

    private val winner = wheelSegmentList[1]


    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun showsCorrectWinnerWheelSegment() = runTest {
        // Given - some wheel segments
        repository.addMultiple(wheelSegmentList)

        // When - on startup
        setContent()

        // Then - verify the winner wheel segment is shown
        composeTestRule
            .onNodeWithText(winner.title)
            .assertIsDisplayed()
    }

    @Test
    fun showsDeleteButton_whenMoreThanTwoWheelSegmentsExists() = runTest {
        // Given - some wheel segments
        repository.addMultiple(wheelSegmentList)

        // When - on startup
        setContent()

        // Then - verify the "Delete and Spin" button is visible
        composeTestRule
            .onNodeWithText(stringResource(R.string.button_delete_and_spin))
            .assertIsDisplayed()
    }

    @Test
    fun hidesDeleteButton_whenOnlyTwoWheelSegmentsExists() = runTest {
        // Given - 2 wheel segments
        repository.addMultiple(wheelSegmentMinList)

        // When - on startup
        setContent()

        // Then - verify the "Delete and Spin" button is hidden
        composeTestRule
            .onNodeWithText(stringResource(R.string.button_delete_and_spin))
            .assertIsNotDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            viewModel = ResultsViewModel(
                savedStateHandle = SavedStateHandle(mapOf("winnerWheelSegmentId" to winner.id)),
                getWheelSegmentStreamUseCase = GetWheelSegmentStreamUseCase(repository),
                getWheelSegmentsUseCase = GetWheelSegmentsUseCase(repository),
                deleteWheelSegmentUseCase = DeleteWheelSegmentUseCase(repository)
            )
            ResultsScreen(
                viewModel = viewModel,
                navigateToSpin = {},
                navigateToHome = {}
            )
        }
    }
}
