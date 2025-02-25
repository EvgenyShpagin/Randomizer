package com.random.randomizer.presentation.navigation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onSibling
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
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
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalTestApi::class)
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

    private val wheelSegments = listOf(
        WheelSegment(1, "Title 1", "", null, null),
        WheelSegment(2, "Title 2", "", null, null),
    )

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
    fun homeScreen_navigatesToSpin_whenSpinButtonClicked() = runTest {
        // Add some items to be available to navigate to SpinScreen
        wheelSegmentRepository.addMultiple(wheelSegments)

        // Click spin button to navigate
        composeTestRule
            .onNodeWithText(stringResource(R.string.button_spin), useUnmergedTree = true)
            .performClick()

        // Assert we are on SpinScreen
        assertTrue(navController.isCurrentDestination<Destination.SpinWheel>())
    }

    @Test
    fun spinScreen_navigatesToResults_whenSpinFinished() = runTest {
        // Given - minimum required item count to be able to scroll
        wheelSegmentRepository.addMultiple(wheelSegments)

        navController.navigate(Destination.SpinWheel)

        // Assert it is navigated to Results screen
        composeTestRule.waitUntil(20_000) {
            navController.isCurrentDestination<Destination.SpinWheel>()
        }
    }

    @Test
    fun resultsScreen_navigatesToSpin_whenButtonClicked() = runTest {
        // Given - minimum required item count to be able to scroll
        wheelSegmentRepository.addMultiple(wheelSegments)

        navController.navigate(Destination.Results(wheelSegments.first().id))

        // When - navigate button is clicked
        composeTestRule
            .onNodeWithText(stringResource(R.string.button_spin))
            .performClick()

        // Then - verify we are navigated to spin screen
        assertTrue(navController.isCurrentDestination<Destination.SpinWheel>())
    }

    @Test
    fun resultsScreen_navigatesToHome_whenSystemBackButtonClicked() = runTest {
        // Given - minimum required item count to be able to scroll
        wheelSegmentRepository.addMultiple(wheelSegments)

        navController.navigate(Destination.Results(wheelSegments.first().id))

        // Wait until screen is visible
        composeTestRule.waitUntilExactlyOneExists(
            hasText(stringResource(R.string.results_screen_title))
        )
        // When - back button is clicked
        composeTestRule.runOnUiThread {
            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        // Then - verify current destination is Home
        assertTrue(navController.isCurrentDestination<Destination.Home>())
    }

    @Test
    fun resultsScreen_navigatesToHome_whenNavigationButtonClicked() = runTest {
        // Given - minimum required item count to be able to scroll
        wheelSegmentRepository.addMultiple(wheelSegments)

        navController.navigate(Destination.Results(wheelSegments.first().id))

        // Wait until screen is visible
        composeTestRule.waitUntilExactlyOneExists(
            hasText(stringResource(R.string.results_screen_title))
        )
        // When - back button is clicked
        composeTestRule.onNodeWithText(
            stringResource(R.string.results_screen_title),
            useUnmergedTree = true
        ).onSibling().performClick()

        // Then - verify current destination is Home
        assertTrue(navController.isCurrentDestination<Destination.Home>())
    }

    @Test
    fun homeScreen_navigatesToEdit_whenAddButtonClicked() = runTest {
        // Given - empty list

        // When - button clicked
        composeTestRule
            .onNodeWithContentDescription(stringResource(R.string.cd_add_new_wheel_segment))
            .assertIsDisplayed()
            .performClick()

        // Then - verify current destination is EditWheelSegment
        assertTrue(navController.isCurrentDestination<Destination.Edit>())
    }

    @Test
    fun homeScreen_navigatesToEdit_whenWheelSegmentClicked() = runTest {
        // Given - multiple wheel segments
        wheelSegmentRepository.addMultiple(wheelSegments)

        // When - wheel segment is clicked
        composeTestRule
            .onNodeWithText(wheelSegments.first().title)
            .assertIsDisplayed()
            .performClick()

        // Then - verify current destination is EditWheelSegment
        assertTrue(navController.isCurrentDestination<Destination.Edit>())
    }

    private fun setContent() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            RandomizerNavHost(navController)
        }
    }

    private inline fun <reified T : Destination> NavController.isCurrentDestination(): Boolean {
        return currentBackStackEntry?.destination?.hasRoute(T::class) == true
    }
}