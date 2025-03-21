package com.random.randomizer.presentation.screen.spin

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.filters.MediumTest
import com.random.randomizer.HiltTestActivity
import com.random.randomizer.data.repository.addMultiple
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
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

    private lateinit var viewModel: SpinViewModel


    @Inject
    lateinit var repository: WheelSegmentRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun displaysWheelSegmentList_whenSegmentsPresent() = runTest {
        // Given - some segments
        repository.addMultiple(ShortFakeList)

        setContent()

        // When - items are updated
        composeTestRule.waitUntil {
            viewModel.uiState.value.wheelSegments.isNotEmpty()
        }

        // Then - verify item is displayed
        // (here is checking at least one item because list extends)
        composeTestRule.onAllNodesWithText(ShortFakeList.first().title)
            .onFirst()
            .assertIsDisplayed()
    }

    @Test
    fun doesNotScroll_whenSwiped() = runTest {
        // Given - some segments
        repository.addMultiple(LongFakeList)

        // When - on startup
        setContent()

        // Then - verify there is no scrollable list
        composeTestRule.onNode(hasScrollAction())
            .assertDoesNotExist()
    }

    @Test
    fun fillsScreenSizeByItems_beforeSpin() = runTest {
        // Given - some segments
        repository.addMultiple(ShortFakeList)

        setContent()
        // When - list of segments will be extended
        composeTestRule.waitUntil {
            viewModel.uiState.value.wheelSegments.count() > ShortFakeList.count()
        }

        val displayMetrics = composeTestRule.activity.resources.displayMetrics
        val screenHeightDp = (displayMetrics.heightPixels / displayMetrics.density).dp

        // Then - verify the list height fills all available space
        composeTestRule
            .onNodeWithTag("Spin Segment List")
            .assertHeightIsAtLeast(screenHeightDp)
    }

    @Test
    fun startsSpin_afterShortDelay() = runTest {
        // Given - long segment list
        repository.addMultiple(ShortFakeList)

        composeTestRule.mainClock.autoAdvance = false

        setContent()

        // When - in a second after shouldBeSpinned is set to true
        composeTestRule.waitUntil { viewModel.uiState.value.shouldBeSpinned }
        // and list will be shown
        composeTestRule.mainClock.advanceTimeBy(1000)

        // Take offset of first item with "fake0" title
        val firstFake0ItemWindowPosition = getFirstFake0ItemWindowPosition()
        composeTestRule.mainClock.advanceTimeBy(200)
        val firstItemWindowPositionAfterDelay = getFirstFake0ItemWindowPosition()

        // Then - verify position of the first item is changed (or it has gone)
        assertNotEquals(firstFake0ItemWindowPosition, firstItemWindowPositionAfterDelay)
    }

    private fun getFirstFake0ItemWindowPosition(): Offset {
        return composeTestRule
            .onAllNodesWithText(ShortFakeList.first().title)
            .onFirst()
            .fetchSemanticsNode()
            .positionInWindow
    }

    @Test
    fun spinsToItemAndCenterIt() = runTest {
        // Given - long segment list
        repository.addMultiple(LongFakeList)

        composeTestRule.mainClock.autoAdvance = false

        setContent()

        // When - after scrolling
        composeTestRule.waitUntil(2000) { viewModel.uiState.value.shouldBeSpinned }
        composeTestRule.mainClock.advanceTimeBy(20000) // Max scroll time

        val uiState = viewModel.uiState.value
        val centerItem = uiState.wheelSegments[uiState.targetIndex]

        val centerNode = composeTestRule
            .onNodeWithText(centerItem.title)
            .fetchSemanticsNode()

        val listContentBoundsInWindow = composeTestRule
            .onNodeWithTag("Spin Segment List")
            .fetchSemanticsNode()
            .boundsInWindow

        val expectedNodeCenterY = listContentBoundsInWindow.center.y
        val actualNodeCenterY = centerNode.positionOnScreen.y + centerNode.size.height / 2f

        // Then - verify the center of the item is in the center of the screen
        assertEquals(
            expectedNodeCenterY,
            actualNodeCenterY,
            4f
        )
    }

    private fun setContent() {
        composeTestRule.setContent {
            viewModel = hiltViewModel()
            SpinScreen(
                viewModel = viewModel,
                transitionDurationMs = 0,
                navigateToResults = {}
            )
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