package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.repository.WheelSegmentRepository
import javax.inject.Inject

class FixSavedWheelSegmentUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    suspend operator fun invoke(wheelSegmentId: Int) {
        val wheelSegment = wheelSegmentRepository.get(wheelSegmentId)!!
        val fixedWheelSegment = wheelSegment.copy(
            title = wheelSegment.title.fixWhitespaces(),
            description = wheelSegment.description.fixWhitespaces()
        )
        wheelSegmentRepository.update(fixedWheelSegment)
    }

    private fun String.fixWhitespaces(): String {
        return trim().replace(ExtraWhitespaceRegex, " ")
    }

    private companion object {
        val ExtraWhitespaceRegex = "\\s{2,}".toRegex()
    }
}