package com.random.randomizer.presentation.core

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.random.randomizer.HiltTestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ComponentsTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Test
    fun statefulContent_showsLoading_whenLoading() {
        setStatefulContent(isLoading = true, isEmpty = false)

        composeTestRule
            .onNodeWithText(LOADING_STATE_TEXT)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(CONTENT_TEXT)
            .assertIsNotDisplayed()
    }

    @Test
    fun statefulContent_showsEmpty_whenEmptyAndNotLoading() {
        setStatefulContent(isLoading = false, isEmpty = true)

        composeTestRule
            .onNodeWithText(EMPTY_STATE_TEXT)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(CONTENT_TEXT)
            .assertIsNotDisplayed()
    }

    @Test
    fun statefulContent_showsContent_whenNotEmptyAndNotLoading() {
        setStatefulContent(isLoading = false, isEmpty = false)

        composeTestRule
            .onNodeWithText(CONTENT_TEXT)
            .assertIsDisplayed()
    }

    @Test
    fun statefulContent_updatesContent_whenStateChanges() {
        var isLoading by mutableStateOf(true)
        var isEmpty by mutableStateOf(true)

        composeTestRule.setContent {
            StatefulContent(
                isLoading = isLoading,
                loadingStateContent = {
                    Text(LOADING_STATE_TEXT)
                },
                isEmpty = isEmpty,
                emptyStateContent = {
                    Text(EMPTY_STATE_TEXT)
                },
                content = {
                    Text(CONTENT_TEXT)
                }
            )
        }

        isLoading = false

        composeTestRule
            .onNodeWithText(EMPTY_STATE_TEXT)
            .assertIsDisplayed()

        isEmpty = false

        composeTestRule
            .onNodeWithText(CONTENT_TEXT)
            .assertIsDisplayed()
    }

    private fun setStatefulContent(
        isLoading: Boolean,
        isEmpty: Boolean
    ) {
        composeTestRule.setContent {
            StatefulContent(
                isLoading = isLoading,
                loadingStateContent = {
                    Text(LOADING_STATE_TEXT)
                },
                isEmpty = isEmpty,
                emptyStateContent = {
                    Text(EMPTY_STATE_TEXT)
                },
                content = {
                    Text(CONTENT_TEXT)
                }
            )
        }
    }

    private companion object {
        const val LOADING_STATE_TEXT = "Loading"
        const val EMPTY_STATE_TEXT = "Empty"
        const val CONTENT_TEXT = "Content"
    }
}