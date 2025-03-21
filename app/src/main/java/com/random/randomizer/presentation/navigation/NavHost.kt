package com.random.randomizer.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.random.randomizer.presentation.screen.edit.EditScreen
import com.random.randomizer.presentation.screen.home.HomeScreen
import com.random.randomizer.presentation.screen.results.ResultsScreen
import com.random.randomizer.presentation.screen.spin.SpinScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RandomizerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Destination.Home,
            modifier = modifier
        ) {
            composable<Destination.Home> {
                HomeScreen(
                    animatedVisibilityScope = this,
                    navigateToSpin = {
                        navController.navigate(Destination.SpinWheel)
                    },
                    navigateToEdit = { wheelSegmentId ->
                        navController.navigate(Destination.Edit(wheelSegmentId))
                    },
                    viewModel = hiltViewModel()
                )
            }
            composable<Destination.SpinWheel>(
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    ) + fadeIn(animationSpec = tween(300))
                },
                popExitTransition = {
                    slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    ) + fadeOut(animationSpec = tween(300))
                }
            ) {
                SpinScreen(
                    navigateToResults = { winnerId ->
                        navController.navigate(Destination.Results(winnerId))
                    },
                    transitionDurationMs = 300,
                    viewModel = hiltViewModel()
                )
            }
            composable<Destination.Results> {
                ResultsScreen(
                    animatedVisibilityScope = this,
                    navigateToHome = {
                        navController.popBackStack(
                            Destination.Home,
                            inclusive = false
                        )
                    },
                    navigateToSpin = {
                        navController.navigate(Destination.SpinWheel) {
                            popUpTo(Destination.SpinWheel) {
                                inclusive = true
                            }
                        }
                    },
                    viewModel = hiltViewModel()
                )
            }
            composable<Destination.Edit> {
                EditScreen(
                    animatedVisibilityScope = this,
                    navigateBack = {
                        navController.navigateUp()
                    },
                    viewModel = hiltViewModel()
                )
            }
        }
    }
}