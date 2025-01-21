package com.random.randomizer.presentation.navigation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.filters.LargeTest
import com.random.randomizer.HiltTestActivity
import com.random.randomizer.R
import com.random.randomizer.data.repository.addMultiple
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.test_util.stringResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@LargeTest
@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var wheelSegmentRepository: WheelSegmentRepository

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()
        setContent()
    }

    @Test
    fun navHost_showsHomeScreen_atStart() {
        // Assert HomeScreen title is displayed
        composeTestRule
            .onNodeWithText(stringResource(R.string.home_screen_title))
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_navigatesToEdit_whenEditButtonClicked() {
        // Click on edit button
        composeTestRule
            .onNodeWithContentDescription(stringResource(R.string.cd_edit_wheel_segments))
            .performClick()

        // Make sure the current screen is EditScreen
        composeTestRule
            .onNodeWithText(stringResource(R.string.edit_screen_title))
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_navigatesToSpin_whenSpinButtonClicked() = runTest {
        // Add some items to be available to navigate to SpinScreen
        val fewWheelSegments = listOf(
            WheelSegment(1, "Title 1", "", null, null),
            WheelSegment(2, "Title 2", "", null, null),
        )
        wheelSegmentRepository.addMultiple(fewWheelSegments)

        // Click spin button to navigate
        composeTestRule
            .onNodeWithText(stringResource(R.string.button_spin), useUnmergedTree = true)
            .performClick()

        // Assert we are on SpinScreen
        assertEquals(
            Destination.SpinWheel::class.qualifiedName,
            navController.currentDestination?.route
        )
    }

    private fun setContent() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            RandomizerNavHost(navController)
        }
    }
}