package com.random.randomizer.presentation.screen.spin

import com.random.randomizer.presentation.core.WheelSegmentUiState

fun List<WheelSegmentUiState>.extendTo(count: Int): List<WheelSegmentUiState> {
    return this * (count / this.size + 1)
}

private operator fun <T> List<T>.times(count: Int): List<T> {
    return List(count) { this }.flatten()
}