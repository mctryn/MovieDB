package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.test.runTest
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

/**
 * Unit tests for GetPopularMoviesUseCase.
 * 
 * Tests the use case that fetches popular movies from repository.
 * Following TDD - RED phase (tests first, implementation later).
 */
@RunWith(MockitoJUnitRunner::class)
class GetPopularMoviesUseCaseTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var useCase: GetPopularMoviesUseCase

    @Before
    fun setup() {
        useCase = GetPopularMoviesUseCase(repository)
    }

    @Test
    fun `invoke should return movies from repository on success`() = runTest {
        // Given
        val expectedMovies = listOf(
            Movie(id = 1, title = "Movie 1", overview = "Overview 1", 
                  posterPath = "/poster1.jpg", releaseDate = "2024-01-01", 
                  voteAverage = 7.5, voteCount = 1000, isFavorite = false),
            Movie(id = 2, title = "Movie 2", overview = "Overview 2", 
                  posterPath = "/poster2.jpg", releaseDate = "2024-01-02", 
                  voteAverage = 8.0, voteCount = 2000, isFavorite = false)
        )
        whenever(repository.getPopularMovies(1)).thenReturn(Result.success(expectedMovies))

        // When
        val result = useCase(1)

        // Then
        assert(result.isSuccess)
        assert(result.getOrNull()?.size == 2)
        verify(repository).getPopularMovies(1)
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        whenever(repository.getPopularMovies(1)).thenReturn(Result.failure(exception))

        // When
        val result = useCase(1)

        // Then
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Network error")
        verify(repository).getPopularMovies(1)
    }

    @Test
    fun `invoke should pass page parameter to repository`() = runTest {
        // Given
        whenever(repository.getPopularMovies(5)).thenReturn(Result.success(emptyList()))

        // When
        useCase(5)

        // Then
        verify(repository).getPopularMovies(5)
    }
}