package com.random.randomizer.test_util

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.rules.ActivityScenarioRule

fun stringResource(@StringRes id: Int): String {
    val context = getApplicationContext<Context>()
    return context.getString(id)
}

fun stringResource(@StringRes id: Int, vararg formatArgs: Any): String {
    val context = getApplicationContext<Context>()
    return context.getString(id, *formatArgs)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun <T : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<T>, T>.setContentWithSharedTransition(
    content: @Composable SharedTransitionScope.(AnimatedVisibilityScope) -> Unit
) {
    setContent {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                content(this)
            }
        }
    }
}