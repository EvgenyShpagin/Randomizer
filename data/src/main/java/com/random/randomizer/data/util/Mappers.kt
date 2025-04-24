package com.random.randomizer.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.random.randomizer.data.model.WheelSegment as DataSegment
import com.random.randomizer.domain.model.WheelSegment as DomainSegment

fun DataSegment.toDomain(): DomainSegment {
    return DomainSegment(
        id = id,
        title = title,
        description = description,
        thumbnail = thumbnail,
        customColor = customColor
    )
}

fun List<DataSegment>.toDomain(): List<DomainSegment> {
    return map { it.toDomain() }
}

@JvmName("segmentFlowToDomain")
fun Flow<DataSegment>.toDomain(): Flow<DomainSegment> {
    return map { it.toDomain() }
}

@JvmName("segmentListFlowToDomain")
fun Flow<List<DataSegment>>.toDomain(): Flow<List<DomainSegment>> {
    return map { it.toDomain() }
}

fun DomainSegment.toData(): DataSegment {
    return DataSegment(
        id = id,
        title = title,
        description = description,
        thumbnail = thumbnail,
        customColor = customColor
    )
}