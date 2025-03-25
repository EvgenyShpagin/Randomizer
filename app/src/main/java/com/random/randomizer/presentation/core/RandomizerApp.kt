package com.random.randomizer.presentation.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.random.randomizer.presentation.navigation.RandomizerNavHost
import com.random.randomizer.presentation.theme.RandomizerTheme

@Composable
fun RandomizerApp() {
    RandomizerTheme {
        val navController = rememberNavController()
        RandomizerNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize()
        )
    }
}
