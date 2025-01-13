package com.random.randomizer.data.source

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wheel_segment")
data class WheelSegment(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val thumbnailId: String?,
    val customColor: Long?
)