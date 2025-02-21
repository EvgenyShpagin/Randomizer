package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import javax.inject.Inject

class FixWheelSegmentUseCase @Inject constructor() {
    operator fun invoke(wheelSegment: WheelSegment): WheelSegment {
        val fixedWheelSegment = wheelSegment.copy(
            title = wheelSegment.title.fixWhitespaces(),
            description = wheelSegment.description.fixWhitespaces()
        )
        return fixedWheelSegment
    }

    private fun String.fixWhitespaces(): String {
        return trim().replace(ExtraWhitespaceRegex, " ")
    }

    private companion object {
        val ExtraWhitespaceRegex = "\\s{2,}".toRegex()
    }
}