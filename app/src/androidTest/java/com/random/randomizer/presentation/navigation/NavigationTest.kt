package com.random.randomizer.presentation.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.filters.LargeTest
import com.random.randomizer.R
import com.random.randomizer.presentation.core.MainActivity
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
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    lateinit var navController: TestNavHostController


    @Before
    fun setup() {
        hiltRule.inject()
        setupNavHost()
    }

    fun setupNavHost() {
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            RandomizerNavHost(navController)
        }
    }

    @Test
    fun navHost_showsHomeScreen_atStart() {
        val topBarText = stringResource(R.string.home_screen_title)
        composeTestRule
            .onNodeWithText(topBarText)
            .assertIsDisplayed()
    }

    @Test
    fun navHost_navigatesToEdit_whenEditButtonClicked() {
        val editButtonContentDescription = stringResource(R.string.cd_edit_wheel_segments)
        val editTopBarText = stringResource(R.string.edit_screen_title)
        composeTestRule
            .onNodeWithContentDescription(editButtonContentDescription)
            .performClick()
        composeTestRule
            .onNodeWithText(editTopBarText)
            .assertIsDisplayed()
    }
}