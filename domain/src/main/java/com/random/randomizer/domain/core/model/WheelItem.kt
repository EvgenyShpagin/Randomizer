package com.random.randomizer.domain.core.model

data class WheelItem(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnailPath: String?,
    val customColor: Long?
)