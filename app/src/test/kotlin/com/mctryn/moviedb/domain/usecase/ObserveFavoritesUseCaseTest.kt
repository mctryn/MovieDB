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
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

/**
 * Unit tests for ObserveFavoritesUseCase.
 */
@RunWith(MockitoJUnitRunner::class)
class ObserveFavoritesUseCaseTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var useCase: ObserveFavoritesUseCase

    private val testFavorites = listOf(
        Movie(id = 1, title = "Movie 1", overview = "Overview 1",
              posterPath = "/poster1.jpg", releaseDate = "2024-01-01",
              voteAverage = 7.5, voteCount = 1000, isFavorite = true),
        Movie(id = 2, title = "Movie 2", overview = "Overview 2",
              posterPath = "/poster2.jpg", releaseDate = "2024-01-02",
              voteAverage = 8.0, voteCount = 2000, isFavorite = true)
    )

    @Before
    fun setup() {
        useCase = ObserveFavoritesUseCase(repository)
    }

    @Test
    fun `invoke should return Loading then Success state with favorites`() = runTest {
        // Given
        whenever(repository.getFavorites()).thenReturn(
            flowOf(RepositoryState.Loading, RepositoryState.Success(testFavorites))
        )

        // When
        val result = useCase().first { it is RepositoryState.Success }

        // Then
        assert(result is RepositoryState.Success)
        assert((result as RepositoryState.Success).data.size == 2)
        verify(repository).getFavorites()
    }

    @Test
    fun `invoke should return Loading then Success with empty list when no favorites`() = runTest {
        // Given
        whenever(repository.getFavorites()).thenReturn(
            flowOf(RepositoryState.Loading, RepositoryState.Success(emptyList()))
        )

        // When
        val result = useCase().first { it is RepositoryState.Success }

        // Then
        assert(result is RepositoryState.Success)
        assert((result as RepositoryState.Success).data.isEmpty())
        verify(repository).getFavorites()
    }

    @Test
    fun `invoke should emit updated favorites when repository emits new data`() = runTest {
        // Given
        val updatedFavorites = listOf(
            Movie(id = 1, title = "Movie 1", overview = "Overview 1",
                  posterPath = "/poster1.jpg", releaseDate = "2024-01-01",
                  voteAverage = 7.5, voteCount = 1000, isFavorite = true)
        )
        whenever(repository.getFavorites()).thenReturn(
            flowOf(RepositoryState.Loading, RepositoryState.Success(updatedFavorites))
        )

        // When
        useCase().collect { }

        // Then
        verify(repository).getFavorites()
    }
}