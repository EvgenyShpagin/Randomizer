package com.random.randomizer.presentation.screen.spin

import androidx.lifecycle.viewModelScope
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.GetWheelSegmentsUseCase
import com.random.randomizer.domain.usecase.SelectRandomWheelSegmentUseCase
import com.random.randomizer.presentation.core.ImmutableStateViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.core.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpinViewModel @Inject constructor(
    private val getWheelSegmentsUseCase: GetWheelSegmentsUseCase,
    private val selectRandomWheelSegmentUseCase: SelectRandomWheelSegmentUseCase
) : ImmutableStateViewModel<SpinUiState, SpinUiEvent, SpinUiEffect>() {

    private val _uiState = MutableStateFlow(SpinUiState())
    override val uiState = _uiState
        .onStart {
            val wheelSegments = getWheelSegmentsUseCase()
            setupWheelSegments(wheelSegments)
            selectWinnerWithDelay(wheelSegments)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = SpinUiState()
        )

    private fun setupWheelSegments(wheelSegments: List<WheelSegment>) {
        _uiState.update { state ->
            state.copy(
                wheelSegments = wheelSegments
                    .map(::toPresentation)
                    .extend(),
                originListSize = wheelSegments.count()
            )
        }
    }

    private fun List<WheelSegmentUiState>.extend(): List<WheelSegmentUiState> {
        val desiredItemCount = (count() * EXTEND_MULTIPLIER).coerceAtLeast(MIN_ITEM_COUNT)
        return extendTo(minCount = desiredItemCount)
    }

    private fun selectWinnerWithDelay(wheelSegments: List<WheelSegment>) {
        viewModelScope.launch {
            delay(1000)
            _uiState.update { state ->
                state.copy(
                    shouldBeSpinned = true,
                    targetIndex = getWinnerIndex(
                        winner = selectRandomWheelSegmentUseCase(),
                        originalSegments = wheelSegments,
                        extendCount = state.wheelSegments.count()
                    )
                )
            }
        }
    }

    // Consider only the items close to the middle
    private fun getWinnerIndex(
        winner: WheelSegment,
        originalSegments: List<WheelSegment>,
        extendCount: Int
    ): Int {
        val originalCount = originalSegments.count()
        val extendMultiplier = extendCount / originalCount
        val winRangeStartIndex = originalCount * (extendMultiplier / 2)
        return originalSegments.indexOf(winner) + winRangeStartIndex
    }

    override fun onEvent(event: SpinUiEvent) {
        when (event) {
            SpinUiEvent.SpinFinished -> onSpinFinished()
        }
    }

    private fun onSpinFinished() = viewModelScope.launch {
        _uiState.updateAndGet { state ->
            state.copy(shouldBeSpinned = false)
        }.apply {
            val winner = wheelSegments[targetIndex]
            delay(1000)
            triggerEffect(SpinUiEffect.NavigateToResults(winner.id))
        }
    }

    private companion object {

        /**
         * A multiplier to extend original list to allow each segment to be spun multiple times,
         * and also select a segment in the middle of the list, which will eliminate the situation
         * when the target item is the last one and centering it will not be possible
         */
        const val EXTEND_MULTIPLIER = 3

        /**
         * Minimal number of the segments to allow longer scrolling
         */
        const val MIN_ITEM_COUNT = 100

    }
}