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
 * Unit tests for GetMovieDetailsUseCase.
 */
@RunWith(MockitoJUnitRunner::class)
class GetMovieDetailsUseCaseTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var useCase: GetMovieDetailsUseCase

    private val testMovie = Movie(
        id = 550,
        title = "Fight Club",
        overview = "A depressed man suffering from insomnia meets a strange soap salesman.",
        posterPath = "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
        releaseDate = "1999-10-15",
        voteAverage = 8.4,
        voteCount = 26280,
        isFavorite = false
    )

    @Before
    fun setup() {
        useCase = GetMovieDetailsUseCase(repository)
    }

    @Test
    fun `invoke should return Loading then Success state with movie`() = runTest {
        whenever(repository.getMovieDetails(550)).thenReturn(
            flowOf(RepositoryState.Loading, RepositoryState.Success(testMovie))
        )

        val result = useCase(550).first { it is RepositoryState.Success }

        assert(result is RepositoryState.Success)
        assert((result as RepositoryState.Success).data.title == "Fight Club")
        verify(repository).getMovieDetails(550)
    }

    @Test
    fun `invoke should emit Error state when movie not found`() = runTest {
        val errorMessage = "Movie not found"
        whenever(repository.getMovieDetails(999)).thenReturn(
            flowOf(RepositoryState.Loading, RepositoryState.Error(errorMessage))
        )

        val result = useCase(999).first { it is RepositoryState.Error }

        assert(result is RepositoryState.Error)
        assert((result as RepositoryState.Error).message == errorMessage)
        verify(repository).getMovieDetails(999)
    }

    @Test
    fun `invoke should pass movieId to repository`() = runTest {
        whenever(repository.getMovieDetails(123)).thenReturn(
            flowOf(RepositoryState.Loading, RepositoryState.Error("Not found"))
        )

        useCase(123).first()

        verify(repository).getMovieDetails(123)
    }
}
