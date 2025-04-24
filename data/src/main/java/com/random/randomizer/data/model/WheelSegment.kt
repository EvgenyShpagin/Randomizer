package com.random.randomizer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.random.randomizer.domain.model.Image

@Entity(tableName = "wheel_segment")
data class WheelSegment(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: Image?,
    val customColor: Long?
)