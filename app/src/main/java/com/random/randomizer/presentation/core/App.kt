package com.random.randomizer.presentation.core

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.random.randomizer.presentation.screen.home.HomeScreen
import com.random.randomizer.presentation.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        HomeScreen(
            navigateToSpin = {
                // TODO: implement
            },
            navigateToEdit = {
                // TODO: implement
            },
            homeViewModel = viewModel()
        )
    }
}
