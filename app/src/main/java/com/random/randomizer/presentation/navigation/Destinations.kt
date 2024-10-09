package com.random.randomizer.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object Home : Destination

    @Serializable
    data object Edit : Destination

    @Serializable
    data object SpinWheel : Destination
}