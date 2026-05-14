package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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
        whenever(repository.getPopularMovies()).thenReturn(
            flowOf(RepositoryState.Loading, RepositoryState.Success(testMovies))
        )

        val result = useCase().first { it is RepositoryState.Success }

        assert(result is RepositoryState.Success)
        assert((result as RepositoryState.Success).data.size == 2)
        verify(repository).getPopularMovies()
    }

    @Test
    fun `invoke should emit Error state when repository fails`() = runTest {
        val errorMessage = "Network error"
        whenever(repository.getPopularMovies()).thenReturn(
            flowOf(RepositoryState.Loading, RepositoryState.Error(errorMessage))
        )

        val result = useCase().first { it is RepositoryState.Error }

        assert(result is RepositoryState.Error)
        assert((result as RepositoryState.Error).message == errorMessage)
        verify(repository).getPopularMovies()
    }
}
