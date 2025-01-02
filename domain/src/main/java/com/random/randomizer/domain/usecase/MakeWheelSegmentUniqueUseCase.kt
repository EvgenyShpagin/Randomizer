package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.model.contentEquals
import com.random.randomizer.domain.repository.WheelSegmentRepository
import javax.inject.Inject

class MakeWheelSegmentUniqueUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    suspend operator fun invoke(editedWheelSegmentId: Int) {
        val wheelSegment = wheelSegmentRepository.get(editedWheelSegmentId)!!

        val allWheelSegments = wheelSegmentRepository.getAll()
        val equalCount = wheelSegment.countEqual(allWheelSegments)

        if (equalCount == 0) return

        val updatedWheelSegment = wheelSegment.updateCopyIndex { i -> i + 1 }
        wheelSegmentRepository.update(updatedWheelSegment)
    }

    private fun WheelSegment.countEqual(allWheelSegments: List<WheelSegment>): Int {
        return allWheelSegments.count { id != it.id && contentEquals(it) }
    }

    private fun WheelSegment.updateCopyIndex(transform: (Int) -> Int): WheelSegment {
        val copySuffixSequence = CopyIndexSuffixRegex.findAll(title)
        val copySuffix = copySuffixSequence.lastOrNull()?.value

        if (copySuffix == null) {
            val newCopyIndex = transform(0)
            return copy(title = title.appendCopySuffix(newCopyIndex))
        } else {
            val copyIndex = copySuffix.filter { it.isDigit() }.toInt()
            val newCopyIndex = transform(copyIndex)
            return copy(
                title = title
                    .removeSuffix(copySuffix)
                    .appendCopySuffix(newCopyIndex)
            )
        }
    }

    private fun String.appendCopySuffix(index: Int): String {
        return "$this ($index)"
    }

    private companion object {
        val CopyIndexSuffixRegex = " \\(\\d+\\)$".toRegex()
    }
}