package com.random.randomizer.domain.model

data class WheelSegment(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnailPath: String?,
    val customColor: Long?
)

fun WheelSegment.contentEquals(other: WheelSegment): Boolean {
    return this.title == other.title
            && this.description == other.description
            && this.thumbnailPath == other.thumbnailPath
            && this.customColor == other.customColor
}