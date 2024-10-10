package com.random.randomizer.presentation.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.random.randomizer.R
import com.random.randomizer.test_util.testStringResource
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var navController: TestNavHostController


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
        val topBarText = testStringResource(R.string.home_screen_title)
        composeTestRule
            .onNodeWithText(topBarText)
            .assertIsDisplayed()
    }

    @Test
    fun navHost_navigatesToEdit_whenEditButtonClicked() {
        val editButtonContentDescription = testStringResource(R.string.cd_edit_wheel_segments)
        val editTopBarText = testStringResource(R.string.edit_screen_title)
        composeTestRule
            .onNodeWithContentDescription(editButtonContentDescription)
            .performClick()
        composeTestRule
            .onNodeWithText(editTopBarText)
            .assertIsDisplayed()
    }
}