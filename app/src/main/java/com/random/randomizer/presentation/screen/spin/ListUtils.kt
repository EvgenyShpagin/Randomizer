package com.random.randomizer.presentation.screen.spin

import com.random.randomizer.presentation.core.WheelSegmentUiState

fun List<WheelSegmentUiState>.extendTo(minCount: Int): List<WheelSegmentUiState> {
    require(minCount > 1) { "Minimum count should be greater than 1" }
    val repeatCount = (minCount + size - 1) / size
    return this * repeatCount
}

private operator fun <T> List<T>.times(count: Int): List<T> {
    return List(count) { this }.flatten()
}