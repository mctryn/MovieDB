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
class RefreshMovieDetailsUseCaseTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var useCase: RefreshMovieDetailsUseCase

    @Before
    fun setup() {
        useCase = RefreshMovieDetailsUseCase(repository)
    }

    @Test
    fun `invoke should delegate to repository refreshMovieDetails`() = runTest {
        whenever(repository.refreshMovieDetails(550)).thenReturn(Result.success(Unit))

        val result = useCase(550)

        assert(result.isSuccess)
        verify(repository).refreshMovieDetails(550)
    }

    @Test
    fun `invoke should return failure when repository refresh fails`() = runTest {
        val exception = Exception("Refresh failed")
        whenever(repository.refreshMovieDetails(550)).thenReturn(Result.failure(exception))

        val result = useCase(550)

        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Refresh failed")
    }
}
