package com.random.randomizer.presentation.navigation

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
import com.random.randomizer.test_util.stringResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var navController: TestNavHostController


    @Before
    fun setupNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            RandomizerNavHost(navController)
        }
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
}