package com.mctryn.moviedb.data.remote.api

import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class MovieApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: MovieApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // ===== Popular Movies Tests =====

    @Test
    fun getPopularMovies_shouldReturnMovieList() = runTest {
        // Given
        val jsonResponse = """
        {
            "page": 1,
            "results": [
                {
                    "id": 1,
                    "title": "Test Movie 1",
                    "overview": "Overview 1",
                    "poster_path": "/poster1.jpg",
                    "release_date": "2024-01-01",
                    "vote_average": 7.5,
                    "vote_count": 1000
                },
                {
                    "id": 2,
                    "title": "Test Movie 2",
                    "overview": "Overview 2",
                    "poster_path": "/poster2.jpg",
                    "release_date": "2024-02-01",
                    "vote_average": 8.0,
                    "vote_count": 2000
                }
            ],
            "total_pages": 10,
            "total_results": 20
        }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        // When
        val response = apiService.getPopularMovies(page = 1)

        // Then
        assertNotNull(response)
        assertEquals(2, response.results.size)
        assertEquals("Test Movie 1", response.results[0].title)
    }

    @Test
    fun getPopularMovies_emptyResults_shouldReturnEmptyList() = runTest {
        // Given
        val jsonResponse = """
        {
            "page": 1,
            "results": [],
            "total_pages": 0,
            "total_results": 0
        }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        // When
        val response = apiService.getPopularMovies(page = 1)

        // Then
        assertNotNull(response)
        assertTrue(response.results.isEmpty())
    }

    // ===== Search Movies Tests =====

    @Test
    fun searchMovies_shouldReturnMatchingResults() = runTest {
        // Given
        val jsonResponse = """
        {
            "page": 1,
            "results": [
                {
                    "id": 1,
                    "title": "Batman",
                    "overview": "Batman overview",
                    "poster_path": "/batman.jpg",
                    "release_date": "2024-06-01",
                    "vote_average": 8.5,
                    "vote_count": 5000
                }
            ],
            "total_pages": 1,
            "total_results": 1
        }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        // When
        val response = apiService.searchMovies(query = "Batman", page = 1)

        // Then
        assertNotNull(response)
        assertEquals(1, response.results.size)
        assertEquals("Batman", response.results[0].title)
    }

    // ===== Movie Details Tests =====

    @Test
    fun getMovieDetails_shouldReturnMovieDetails() = runTest {
        // Given
        val jsonResponse = """
        {
            "id": 123,
            "title": "Test Movie",
            "overview": "Test Overview",
            "poster_path": "/poster.jpg",
            "release_date": "2024-01-15",
            "vote_average": 7.8,
            "vote_count": 3500,
            "runtime": 120,
            "genres": [{"id": 28, "name": "Action"}],
            "status": "Released"
        }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        // When
        val response = apiService.getMovieDetails(movieId = 123)

        // Then
        assertNotNull(response)
        assertEquals(123, response.id)
        assertEquals("Test Movie", response.title)
        assertEquals(120, response.runtime)
    }

    // ===== Error Handling Tests =====

    @Test
    fun getPopularMovies_serverError_shouldThrowException() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Server Error")
        )

        // When & Then
        try {
            apiService.getPopularMovies(page = 1)
            throw AssertionError("Expected exception was not thrown")
        } catch (e: retrofit2.HttpException) {
            assertEquals(500, e.code())
        }
    }

    @Test
    fun getPopularMovies_networkError_shouldThrowException() = runTest {
        // Given - shutdown server to simulate network error
        mockWebServer.shutdown()

        // When & Then
        try {
            apiService.getPopularMovies(page = 1)
            throw AssertionError("Expected exception was not thrown")
        } catch (e: Exception) {
            // Expected - either IOException or network error
            assertTrue(e.message?.contains("Failed to connect") == true || e is java.io.IOException)
        }
    }
}