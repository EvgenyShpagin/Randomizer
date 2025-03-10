package com.random.randomizer.presentation.test_util

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.random.randomizer.presentation.navigation.Destination
import io.mockk.every
import io.mockk.mockkStatic

inline fun <reified T : Destination> SavedStateHandle.mockkToRoute(route: T) {
    mockkStatic("androidx.navigation.SavedStateHandleKt")
    every { toRoute<T>() } returns route
}