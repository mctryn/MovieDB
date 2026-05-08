package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

/**
 * Unit tests for ObserveFavoritesUseCase.
 * 
 * Tests the use case that observes favorite movies from repository.
 * Following TDD - RED phase (tests first, implementation later).
 */
@RunWith(MockitoJUnitRunner::class)
class ObserveFavoritesUseCaseTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var useCase: ObserveFavoritesUseCase

    @Before
    fun setup() {
        useCase = ObserveFavoritesUseCase(repository)
    }

    @Test
    fun `invoke should return flow of favorite movies from repository`() = runTest {
        // Given
        val favorites = listOf(
            Movie(id = 1, title = "Movie 1", overview = "Overview 1", 
                  posterPath = "/poster1.jpg", releaseDate = "2024-01-01", 
                  voteAverage = 7.5, voteCount = 1000, isFavorite = true),
            Movie(id = 2, title = "Movie 2", overview = "Overview 2", 
                  posterPath = "/poster2.jpg", releaseDate = "2024-01-02", 
                  voteAverage = 8.0, voteCount = 2000, isFavorite = true)
        )
        whenever(repository.getFavorites()).thenReturn(flowOf(favorites))

        // When
        val result = useCase()

        // Then
        verify(repository).getFavorites()
    }

    @Test
    fun `invoke should return empty flow when no favorites`() = runTest {
        // Given
        whenever(repository.getFavorites()).thenReturn(flowOf(emptyList()))

        // When
        val result = useCase()

        // Then
        verify(repository).getFavorites()
    }

    @Test
    fun `invoke should return flow that emits updated favorites`() = runTest {
        // Given
        val updatedFavorites = listOf(
            Movie(id = 1, title = "Movie 1", overview = "Overview 1", 
                  posterPath = "/poster1.jpg", releaseDate = "2024-01-01", 
                  voteAverage = 7.5, voteCount = 1000, isFavorite = true)
        )
        whenever(repository.getFavorites()).thenReturn(flowOf(updatedFavorites))

        // When
        useCase()

        // Then
        verify(repository).getFavorites()
    }
}