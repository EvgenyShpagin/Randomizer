package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.ThemeOption
import com.random.randomizer.domain.repository.ThemeOptionRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetThemeOptionsStreamUseCaseTest {

    // Dependency
    private val repository = mockk<ThemeOptionRepository>()

    // Subject under test
    private val useCase = GetThemeOptionsStreamUseCase(repository)

    private data object ThemeOptionOne : ThemeOption
    private data object ThemeOptionTwo : ThemeOption

    @Test
    fun returnsRepositoryThemeOptions() = runTest {
        // Given - some theme options
        coEvery {
            repository.getThemeOptionsStream()
        } returns flowOf(listOf(ThemeOptionOne, ThemeOptionTwo))

        // When - on invoke
        val actualThemeOptions = useCase().first()

        // Then - verify got default theme options
        val expectedThemeOptions = listOf(ThemeOptionOne, ThemeOptionTwo)
        assertEquals(expectedThemeOptions, actualThemeOptions)
    }
}