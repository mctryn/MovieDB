package com.mctryn.moviedb.data.datasource

import com.mctryn.moviedb.data.remote.api.MovieApiService
import com.mctryn.moviedb.data.remote.dto.MovieDetailDto
import com.mctryn.moviedb.data.remote.dto.MovieDto
import com.mctryn.moviedb.data.remote.dto.MovieListResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest {

    @Mock
    private lateinit var apiService: MovieApiService

    private lateinit var remoteDataSource: RemoteDataSource

    private val testMovieDto = MovieDto(
        id = 1,
        title = "Test Movie",
        overview = "Test Overview",
        posterPath = "/poster.jpg",
        releaseDate = "2024-01-15",
        voteAverage = 7.5,
        voteCount = 1000
    )

    private val testMovieDetailDto = MovieDetailDto(
        id = 1,
        title = "Test Movie",
        overview = "Test Overview",
        posterPath = "/poster.jpg",
        releaseDate = "2024-01-15",
        voteAverage = 7.5,
        voteCount = 1000,
        genres = emptyList(),
        runtime = 120,
        status = "Released"
    )

    @Before
    fun setup() {
        remoteDataSource = RemoteDataSource(apiService)
    }

    @Test
    fun `getPopularMovies should return movies from API`() = runTest {
        // Given
        val response = MovieListResponse(
            page = 1,
            results = listOf(testMovieDto),
            totalPages = 1,
            totalResults = 1
        )
        whenever(apiService.getPopularMovies(page = 1)).thenReturn(response)

        // When
        val result = remoteDataSource.getPopularMovies(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("Test Movie", result.getOrNull()?.get(0)?.title)
    }

    @Test
    fun `getPopularMovies should return failure on API error`() = runTest {
        // Given
        whenever(apiService.getPopularMovies(page = 1)).thenThrow(RuntimeException("API Error"))

        // When
        val result = remoteDataSource.getPopularMovies(1)

        // Then
        assertTrue(result.isFailure)
    }


    @Test
    fun `getMovieDetails should return movie from API`() = runTest {
        // Given
        whenever(apiService.getMovieDetails(movieId = 1)).thenReturn(testMovieDetailDto)

        // When
        val result = remoteDataSource.getMovieDetails(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("Test Movie", result.getOrNull()?.title)
    }

    @Test
    fun `getMovieDetails should return failure on API error`() = runTest {
        // Given
        whenever(apiService.getMovieDetails(movieId = 1)).thenThrow(RuntimeException("API Error"))

        // When
        val result = remoteDataSource.getMovieDetails(1)

        // Then
        assertTrue(result.isFailure)
    }

}