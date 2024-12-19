package com.random.randomizer.presentation.screen.spin

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.filters.MediumTest
import com.random.randomizer.HiltTestActivity
import com.random.randomizer.data.addMultiple
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
class SpinScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    lateinit var viewModel: SpinViewModel


    @Inject
    lateinit var repository: WheelSegmentRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun displaysWheelSegmentList_whenSegmentsPresent() = runTest {
        repository.addMultiple(ShortFakeList)

        setContent()

        composeTestRule
            .onAllNodesWithText("fake0")
            .assertAny(hasText("fake0"))
    }

    @Test
    fun doesNotScroll_whenSwiped() = runTest {
        repository.addMultiple(LongFakeList)

        setContent()

        composeTestRule.onRoot()
            .performTouchInput { swipeUp() }

        composeTestRule.onNodeWithText("fake0").assertIsDisplayed()
    }

    @Test
    fun fillsScreenSizeByItems_beforeSpin() = runTest {
        repository.addMultiple(ShortFakeList)

        setContent()
        // Wait some time before content will be updated
        composeTestRule.mainClock.advanceTimeBy(500)

        val displayMetrics = composeTestRule.activity.resources.displayMetrics
        val screenHeightDp = (displayMetrics.heightPixels / displayMetrics.density).dp

        composeTestRule
            .onNodeWithTag("Spin Segment List")
            .assertHeightIsAtLeast(screenHeightDp)
    }

    @Test
    fun containsEachSegmentAtLeastTwice_beforeSpin() = runTest {
        repository.addMultiple(LongFakeList)

        setContent()

        // Wait some time before content will be updated
        composeTestRule.mainClock.advanceTimeBy(500)

        val firstItemCount = viewModel.uiState.value.wheelSegments.count { it.title == "fake0" }
        val lastItemCount = viewModel.uiState.value.wheelSegments.count { it.title == "fake99" }
        assertTrue(firstItemCount >= 2)
        assertTrue(lastItemCount >= 2)
    }

    @Test
    fun startsSpin_afterSecondDelay() = runTest {
        repository.addMultiple(LongFakeList)

        setContent()
        composeTestRule
            .onNodeWithText("fake0")
            .assertIsDisplayed()

        composeTestRule.waitUntil(timeoutMillis = 1000 + 1000) {
            composeTestRule.onAllNodesWithText("fake0")
                .fetchSemanticsNodes().count() == 0
        }

        composeTestRule
            .onNodeWithText("fake0")
            .assertIsNotDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            viewModel = hiltViewModel()
            SpinScreen(viewModel = viewModel)
        }
    }

    companion object {
        val ShortFakeList = List(2) { i ->
            WheelSegment(i, "fake$i", "", null, null)
        }

        val LongFakeList = List(100) { i ->
            WheelSegment(i, "fake$i", "", null, null)
        }
    }
}