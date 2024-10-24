package com.random.randomizer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.random.randomizer.presentation.screen.edit.EditScreen
import com.random.randomizer.presentation.screen.home.HomeScreen

@Composable
fun RandomizerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Home,
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
                homeViewModel = viewModel()
            )
        }
        composable<Destination.Edit> {
            EditScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                viewModel = viewModel()
            )
        }
        composable<Destination.SpinWheel> {
            // TODO: implement
        }
    }
}