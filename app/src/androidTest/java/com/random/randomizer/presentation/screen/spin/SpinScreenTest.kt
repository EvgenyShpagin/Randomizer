package com.random.randomizer.presentation.screen.spin

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.presentation.core.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SpinScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var mappers: SpinMappers

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun spinScreen_displaysWheelSegmentList_whenSegmentsPresent() {
        val getWheelSegmentStreamUseCase = mockk<GetWheelSegmentsStreamUseCase>()

        every {
            getWheelSegmentStreamUseCase()
        } returns flow {
            emit(
                listOf(
                    WheelSegment(1, "fake", "", null, null),
                    WheelSegment(2, "fake2", "", null, null)
                ),
            )
        }

        val viewModel = SpinViewModel(getWheelSegmentStreamUseCase, mappers)

        composeTestRule.activity.setContent {
            SpinScreen(viewModel = viewModel)
        }

        composeTestRule
            .onAllNodesWithText("fake")
            .assertAny(hasText("fake"))
    }

    @Test
    fun spinScreen_doesNotScroll_whenUserScrolls() {
        val getWheelSegmentStreamUseCase = mockk<GetWheelSegmentsStreamUseCase>()

        every {
            getWheelSegmentStreamUseCase()
        } returns flow {
            emit(LongFakeList)
        }

        val viewModel = SpinViewModel(getWheelSegmentStreamUseCase, mappers)

        composeTestRule.activity.setContent {
            SpinScreen(viewModel = viewModel)
        }

        composeTestRule.onRoot()
            .performTouchInput { swipeUp() }

        composeTestRule.onNodeWithText("fake0").assertIsDisplayed()
    }

    @Test
    fun spinScreen_fillsAllScreenSizeByItems_beforeSpin() {
        val getWheelSegmentStreamUseCase = mockk<GetWheelSegmentsStreamUseCase>()

        every {
            getWheelSegmentStreamUseCase()
        } returns flow {
            val shortList = LongFakeList.subList(0, 2)
            emit(shortList)
        }

        val viewModel = SpinViewModel(getWheelSegmentStreamUseCase, mappers)

        composeTestRule.activity.setContent {
            SpinScreen(viewModel = viewModel)
        }

        // Wait some time before content will be updated
        composeTestRule.mainClock.advanceTimeBy(500)

        val displayMetrics = composeTestRule.activity.resources.displayMetrics
        val screenHeightDp = (displayMetrics.heightPixels / displayMetrics.density).dp

        composeTestRule
            .onNodeWithTag("Spin Segment List")
            .assertHeightIsAtLeast(screenHeightDp)
    }

    @Test
    fun spinScreen_containsEachWheelSegmentAtLeastTwice_beforeSpin() {
        val getWheelSegmentStreamUseCase = mockk<GetWheelSegmentsStreamUseCase>()

        every {
            getWheelSegmentStreamUseCase()
        } returns flow {
            emit(LongFakeList)
        }

        val viewModel = SpinViewModel(getWheelSegmentStreamUseCase, mappers)

        composeTestRule.activity.setContent {
            SpinScreen(viewModel = viewModel)
        }

        // Wait some time before content will be updated
        composeTestRule.mainClock.advanceTimeBy(500)

        val fake0SegmentCount = composeTestRule
            .onAllNodesWithText("fake0")
            .fetchSemanticsNodes()
            .count()

        assertTrue(fake0SegmentCount >= 2)
    }

    companion object {
        val LongFakeList = List(100) { i ->
            WheelSegment(i, "fake$i", "", null, null)
        }
    }
}