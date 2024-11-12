package com.random.randomizer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.random.randomizer.presentation.screen.edit.EditScreen
import com.random.randomizer.presentation.screen.home.HomeScreen
import com.random.randomizer.presentation.screen.spin.SpinScreen

@Composable
fun RandomizerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destination.SpinWheel,
        modifier = modifier
    ) {
        composable<Destination.Home> {
            HomeScreen(
                navigateToSpin = {
                    navController.navigate(Destination.SpinWheel)
                },
                navigateToEdit = {
                    navController.navigate(Destination.Edit)
                },
                viewModel = hiltViewModel()
            )
        }
        composable<Destination.Edit> {
            EditScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                viewModel = hiltViewModel()
            )
        }
        composable<Destination.SpinWheel> {
            SpinScreen(
                viewModel = hiltViewModel()
            )
        }
    }
}