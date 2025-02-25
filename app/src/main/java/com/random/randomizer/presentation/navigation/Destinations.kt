package com.random.randomizer.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object Home : Destination

    @Serializable
    data class Edit(val wheelSegmentId: Int?) : Destination

    @Serializable
    data object SpinWheel : Destination

    @Serializable
    data class Results(val winnerWheelSegmentId: Int) : Destination
}