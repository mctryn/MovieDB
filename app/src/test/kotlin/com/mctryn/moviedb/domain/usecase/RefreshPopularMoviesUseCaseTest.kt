package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class RefreshPopularMoviesUseCaseTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var useCase: RefreshPopularMoviesUseCase

    @Before
    fun setup() {
        useCase = RefreshPopularMoviesUseCase(repository)
    }

    @Test
    fun `invoke should delegate to repository refreshPopularMovies`() = runTest {
        whenever(repository.refreshPopularMovies()).thenReturn(Result.success(Unit))

        val result = useCase()

        assert(result.isSuccess)
        verify(repository).refreshPopularMovies()
    }

    @Test
    fun `invoke should return failure when repository refresh fails`() = runTest {
        val exception = Exception("Refresh failed")
        whenever(repository.refreshPopularMovies()).thenReturn(Result.failure(exception))

        val result = useCase()

        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Refresh failed")
    }
}
