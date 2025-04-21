package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.ThemeOption
import com.random.randomizer.domain.repository.ThemeOptionRepository
import javax.inject.Inject

class SetThemeOptionUseCase @Inject constructor(
    private val themeOptionRepository: ThemeOptionRepository
) {
    suspend operator fun invoke(themeOption: ThemeOption) {
        themeOptionRepository.setThemeOption(themeOption)
    }
}