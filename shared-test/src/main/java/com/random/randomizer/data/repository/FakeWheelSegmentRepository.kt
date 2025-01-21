package com.random.randomizer.data.repository

import androidx.annotation.VisibleForTesting
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FakeWheelSegmentRepository @Inject constructor() : WheelSegmentRepository {

    private val wheelSegmentListFlow = MutableStateFlow(emptyList<WheelSegment>())
    private val wheelSegmentFlow = MutableStateFlow<WheelSegment?>(null)

    private var requestedMissingWheelSegmentId: Int? = null

    private val wheelSegmentList get() = wheelSegmentListFlow.value
    private val wheelSegment get() = wheelSegmentFlow.value

    override suspend fun add(segment: WheelSegment): Int {
        val newSegment = segment.withCorrectId()
        wheelSegmentListFlow.update { segments -> segments + newSegment }
        updateWheelSegmentFlow()
        return newSegment.id
    }

    private fun WheelSegment.withCorrectId(): WheelSegment {
        val generateId = id < 0 || wheelSegmentList.any { it.id == id }
        return if (generateId) {
            copy(id = getNewSegmentId())
        } else {
            this
        }
    }

    private fun getNewSegmentId(): Int {
        return wheelSegmentList.maxOfOrNull { segment -> segment.id + 1 } ?: 0
    }

    private fun updateWheelSegmentFlow() {
        wheelSegmentFlow.update { segment ->
            when {
                segment != null -> {
                    wheelSegmentList.find { it.id == segment.id }
                }

                requestedMissingWheelSegmentId != null -> {
                    wheelSegmentList.find { it.id == requestedMissingWheelSegmentId }
                        .also { requestedMissingWheelSegmentId = null }
                }

                else -> {
                    null
                }
            }
        }
    }

    override suspend fun getAll(): List<WheelSegment> {
        return wheelSegmentList
    }

    override fun getAllStream(): Flow<List<WheelSegment>> {
        return wheelSegmentListFlow
    }

    override suspend fun get(segmentId: Int): WheelSegment? {
        return wheelSegmentList.find { segment -> segment.id == segmentId }
    }

    override fun getStream(segmentId: Int): Flow<WheelSegment?> {
        val wheelSegment = wheelSegment
        if (wheelSegment == null || wheelSegment.id != segmentId) {
            val gotSegment = runBlocking { get(segmentId) }
            wheelSegmentFlow.update { gotSegment }
            if (gotSegment == null) {
                requestedMissingWheelSegmentId = segmentId
            }
        }

        return wheelSegmentFlow
    }

    override suspend fun update(segment: WheelSegment) {
        // Replace new with existing by id
        wheelSegmentListFlow.update { segments ->
            segments.map {
                if (it.id == segment.id) {
                    segment
                } else {
                    it
                }
            }
        }
        // Update wheelSegment if it has a subscriber
        if (segment.id == wheelSegment?.id) {
            wheelSegmentFlow.update { segment }
        }
    }

    override suspend fun deleteById(segmentId: Int) {
        // As wheelSegment being subscribed is going to be deleted set null
        if (wheelSegment?.id == segmentId) {
            wheelSegmentFlow.update { null }
        }

        val segmentToDelete = wheelSegmentList.find { segment -> segment.id == segmentId }
            ?: return
        wheelSegmentListFlow.update { segments -> segments - segmentToDelete }
    }
}

@VisibleForTesting
suspend fun WheelSegmentRepository.addMultiple(segments: List<WheelSegment>) {
    segments.forEach { segment ->
        add(segment)
    }
}