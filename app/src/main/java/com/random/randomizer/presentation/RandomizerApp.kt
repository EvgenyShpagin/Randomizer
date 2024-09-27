package com.random.randomizer.presentation

import androidx.compose.runtime.Composable
import com.random.randomizer.presentation.home.HomeScreen
import com.random.randomizer.presentation.theme.RandomizerTheme

@Composable
fun RandomizerApp() {
    RandomizerTheme {
        HomeScreen()
    }
}