package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

/**
 * Unit tests for ToggleFavoriteUseCase.
 * 
 * Tests the use case that toggles movie favorite status.
 * Following TDD - RED phase (tests first, implementation later).
 */
@RunWith(MockitoJUnitRunner::class)
class ToggleFavoriteUseCaseTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var useCase: ToggleFavoriteUseCase

    @Before
    fun setup() {
        useCase = ToggleFavoriteUseCase(repository)
    }

    @Test
    fun `invoke should return success when toggle succeeds`() = runTest {
        // Given
        whenever(repository.toggleFavorite(550)).thenReturn(Result.success(Unit))

        // When
        val result = useCase(550)

        // Then
        assert(result.isSuccess)
        verify(repository).toggleFavorite(550)
    }

    @Test
    fun `invoke should return failure when toggle fails`() = runTest {
        // Given
        val exception = Exception("Failed to toggle favorite")
        whenever(repository.toggleFavorite(550)).thenReturn(Result.failure(exception))

        // When
        val result = useCase(550)

        // Then
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Failed to toggle favorite")
        verify(repository).toggleFavorite(550)
    }

    @Test
    fun `invoke should pass movieId to repository`() = runTest {
        // Given
        whenever(repository.toggleFavorite(123)).thenReturn(Result.success(Unit))

        // When
        useCase(123)

        // Then
        verify(repository).toggleFavorite(123)
    }
}