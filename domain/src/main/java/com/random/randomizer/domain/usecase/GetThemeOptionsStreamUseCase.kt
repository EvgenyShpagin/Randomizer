package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.ThemeOption
import com.random.randomizer.domain.repository.ThemeOptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeOptionsStreamUseCase @Inject constructor(
    private val themeOptionRepository: ThemeOptionRepository
) {
    operator fun invoke(): Flow<List<ThemeOption>> {
        return themeOptionRepository.getThemeOptionsStream()
    }
}