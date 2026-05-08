package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.model.Movie
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
 * Unit tests for GetMovieDetailsUseCase.
 * 
 * Tests the use case that fetches movie details from repository.
 * Following TDD - RED phase (tests first, implementation later).
 */
@RunWith(MockitoJUnitRunner::class)
class GetMovieDetailsUseCaseTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var useCase: GetMovieDetailsUseCase

    @Before
    fun setup() {
        useCase = GetMovieDetailsUseCase(repository)
    }

    @Test
    fun `invoke should return movie from repository on success`() = runTest {
        // Given
        val movie = Movie(
            id = 550,
            title = "Fight Club",
            overview = "A depressed man suffering from insomnia meets a strange soap salesman.",
            posterPath = "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
            releaseDate = "1999-10-15",
            voteAverage = 8.4,
            voteCount = 26280,
            isFavorite = false
        )
        whenever(repository.getMovieDetails(550)).thenReturn(Result.success(movie))

        // When
        val result = useCase(550)

        // Then
        assert(result.isSuccess)
        assert(result.getOrNull()?.title == "Fight Club")
        verify(repository).getMovieDetails(550)
    }

    @Test
    fun `invoke should return failure when movie not found`() = runTest {
        // Given
        val exception = Exception("Movie not found")
        whenever(repository.getMovieDetails(999)).thenReturn(Result.failure(exception))

        // When
        val result = useCase(999)

        // Then
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Movie not found")
        verify(repository).getMovieDetails(999)
    }

    @Test
    fun `invoke should pass movieId to repository`() = runTest {
        // Given
        whenever(repository.getMovieDetails(123)).thenReturn(Result.failure(Exception("Not found")))

        // When
        useCase(123)

        // Then
        verify(repository).getMovieDetails(123)
    }
}