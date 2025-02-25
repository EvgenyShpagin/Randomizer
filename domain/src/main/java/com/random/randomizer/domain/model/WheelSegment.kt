package com.random.randomizer.domain.model

data class WheelSegment(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: Image?,
    val customColor: Long?
)