package com.random.randomizer.presentation.screen.spin

import androidx.lifecycle.viewModelScope
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpinViewModel @Inject constructor(
    private val getWheelSegmentsStreamUseCase: GetWheelSegmentsStreamUseCase,
    private val mappers: SpinMappers
) : BaseViewModel<SpinUiState, Nothing, Nothing>(
    initialUiState = SpinUiState()
) {
    init {
        viewModelScope.launch {
            getWheelSegmentsStreamUseCase().collect { wheelSegments ->
                updateState {
                    it.copy(
                        wheelSegments = wheelSegments
                            .map { mappers.toPresentation(it) }
                            .extendTo(100.coerceAtLeast(wheelSegments.count() * 2))
                    )
                }
            }
        }
    }

    override fun onEvent(event: Nothing) {}
}