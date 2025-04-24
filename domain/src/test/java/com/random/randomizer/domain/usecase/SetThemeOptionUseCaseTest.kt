package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.ThemeOption
import com.random.randomizer.domain.repository.ThemeOptionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SetThemeOptionUseCaseTest {

    // Dependency
    private val repository = mockk<ThemeOptionRepository>()

    // Subject under test
    private val useCase = SetThemeOptionUseCase(repository)

    private data object SomeThemeOption : ThemeOption

    @Test
    fun callsRepositorySetThemeOption() = runTest {
        coEvery { repository.setThemeOption(SomeThemeOption) } just runs

        // When - invoked
        useCase(SomeThemeOption)

        // Then - verify theme option was set
        coVerify(exactly = 1) { repository.setThemeOption(SomeThemeOption) }
    }
}