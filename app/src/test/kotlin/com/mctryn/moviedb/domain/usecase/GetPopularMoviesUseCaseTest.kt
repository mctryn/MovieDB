package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
 * Unit tests for GetPopularMoviesUseCase.
 */
@RunWith(MockitoJUnitRunner::class)
class GetPopularMoviesUseCaseTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var useCase: GetPopularMoviesUseCase

    private val testMovies = listOf(
        Movie(id = 1, title = "Movie 1", overview = "Overview 1",
              posterPath = "/poster1.jpg", releaseDate = "2024-01-01",
              voteAverage = 7.5, voteCount = 1000, isFavorite = false),
        Movie(id = 2, title = "Movie 2", overview = "Overview 2",
              posterPath = "/poster2.jpg", releaseDate = "2024-01-02",
              voteAverage = 8.0, voteCount = 2000, isFavorite = false)
    )

    @Before
    fun setup() {
        useCase = GetPopularMoviesUseCase(repository)
    }

    @Test
    fun `invoke should return Loading then Success state with movies`() = runTest {
        // Given
        whenever(repository.getPopularMovies()).thenReturn(
            flowOf(RepositoryState.Loading, RepositoryState.Success(testMovies))
        )

        // When
        val result = useCase().first { it is RepositoryState.Success }

        // Then
        assert(result is RepositoryState.Success)
        assert((result as RepositoryState.Success).data.size == 2)
        verify(repository).getPopularMovies()
    }

    @Test
    fun `invoke should emit Error state when repository fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        whenever(repository.getPopularMovies()).thenReturn(
            flowOf(RepositoryState.Loading, RepositoryState.Error(errorMessage))
        )

        // When
        val result = useCase().first { it is RepositoryState.Error }

        // Then
        assert(result is RepositoryState.Error)
        assert((result as RepositoryState.Error).message == errorMessage)
        verify(repository).getPopularMovies()
    }

    @Test
    fun `refresh should delegate to repository refresh`() = runTest {
        // Given
        whenever(repository.refreshPopularMovies()).thenReturn(Result.success(Unit))

        // When
        val result = useCase.refresh()

        // Then
        assert(result.isSuccess)
        verify(repository).refreshPopularMovies()
    }

    @Test
    fun `refresh should return failure when repository fails`() = runTest {
        // Given
        val exception = Exception("Refresh failed")
        whenever(repository.refreshPopularMovies()).thenReturn(Result.failure(exception))

        // When
        val result = useCase.refresh()

        // Then
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Refresh failed")
    }
}