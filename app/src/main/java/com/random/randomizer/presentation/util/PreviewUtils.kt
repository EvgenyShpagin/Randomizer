package com.random.randomizer.presentation.util

import android.content.res.Configuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.random.randomizer.presentation.core.WheelSegmentUiState

val PreviewWheelSegmentList: List<WheelSegmentUiState>
    get() = List(5) { i ->
        WheelSegmentUiState(
            id = i,
            title = "Title of segment ($i)",
            description = "Its ($i) description",
            customColor = if (i % 2 == 0) Color.Gray else null
        )
    }

class WheelSegmentListParameterProvider : PreviewParameterProvider<List<WheelSegmentUiState>> {
    override val values = sequenceOf(
        listOf(),
        PreviewWheelSegmentList
    )
}

@Preview(
    name = "Day mode",
    group = "night mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Night mode",
    group = "night mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class DayAndNightPreview(val showSystemUi: Boolean = false)