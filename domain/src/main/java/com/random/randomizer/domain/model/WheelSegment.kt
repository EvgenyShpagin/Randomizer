package com.random.randomizer.domain.model

data class WheelSegment(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: Image?,
    val customColor: Long?
)

fun WheelSegment.contentEquals(other: WheelSegment): Boolean {
    return this.title == other.title
            && this.description == other.description
            && this.thumbnail == other.thumbnail
            && this.customColor == other.customColor
}