package com.random.randomizer.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

class FakeWheelSegmentDataSource : WheelSegmentDataSource {

    private val wheelSegmentListFlow = MutableStateFlow(emptyList<WheelSegment>())
    private val wheelSegmentFlow = MutableStateFlow<WheelSegment?>(null)

    private var requestedMissingWheelSegmentId: Int? = null

    private val currentWheelSegmentList get() = wheelSegmentListFlow.value
    private val currentWheelSegment get() = wheelSegmentFlow.value


    override suspend fun getById(id: Int): WheelSegment? {
        return currentWheelSegmentList.find { segment -> segment.id == id }
    }

    override suspend fun insert(wheelSegment: WheelSegment): Long {
        val newSegment = wheelSegment.withCorrectId()
        wheelSegmentListFlow.update { segments -> segments + newSegment }
        updateWheelSegmentFlow()
        return newSegment.id.toLong()
    }

    override suspend fun update(wheelSegment: WheelSegment) {
        // Replace new with existing by id
        wheelSegmentListFlow.update { segments ->
            segments.map {
                if (it.id == wheelSegment.id) {
                    wheelSegment
                } else {
                    it
                }
            }
        }
        // Update wheelSegment if it has a subscriber
        if (wheelSegment.id == currentWheelSegment?.id) {
            wheelSegmentFlow.update { wheelSegment }
        }
    }

    override suspend fun getAll(): List<WheelSegment> {
        return currentWheelSegmentList
    }

    override suspend fun deleteById(id: Int) {
        // As wheelSegment being subscribed is going to be deleted set null
        if (currentWheelSegment?.id == id) {
            wheelSegmentFlow.update { null }
        }

        val segmentToDelete = currentWheelSegmentList.find { segment -> segment.id == id }
            ?: return
        wheelSegmentListFlow.update { segments -> segments - segmentToDelete }
    }

    override fun observeById(id: Int): Flow<WheelSegment> {
        val wheelSegment = currentWheelSegment
        if (wheelSegment == null || wheelSegment.id != id) {
            val gotSegment = runBlocking { getById(id) }
            wheelSegmentFlow.update { gotSegment }
            if (gotSegment == null) {
                requestedMissingWheelSegmentId = id
            }
        }

        return wheelSegmentFlow.filterNotNull()
    }

    override fun observeAll(): Flow<List<WheelSegment>> {
        return wheelSegmentListFlow
    }

    private fun WheelSegment.withCorrectId(): WheelSegment {
        val generateId = id < 0 || currentWheelSegmentList.any { it.id == id }
        return if (generateId) {
            copy(id = getNewSegmentId())
        } else {
            this
        }
    }

    private fun getNewSegmentId(): Int {
        return currentWheelSegmentList.maxOfOrNull { segment -> segment.id + 1 } ?: 0
    }

    private fun updateWheelSegmentFlow() {
        wheelSegmentFlow.update { segment ->
            when {
                segment != null -> {
                    currentWheelSegmentList.find { it.id == segment.id }
                }

                requestedMissingWheelSegmentId != null -> {
                    currentWheelSegmentList.find { it.id == requestedMissingWheelSegmentId }
                        .also { requestedMissingWheelSegmentId = null }
                }

                else -> {
                    null
                }
            }
        }
    }
}