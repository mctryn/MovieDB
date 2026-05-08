package com.mctryn.moviedb.data.datasource

import com.mctryn.moviedb.domain.model.Movie
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.mctryn.moviedb.R
import org.mockito.kotlin.whenever
import java.io.InputStream

@RunWith(MockitoJUnitRunner::class)
class LocalJsonDataSourceTest {

    @Mock
    private lateinit var resourceProvider: ResourceProvider

    @Mock
    private lateinit var inputStream: InputStream

    private lateinit var localJsonDataSource: LocalJsonDataSource

    @Before
    fun setup() {
        localJsonDataSource = LocalJsonDataSource(resourceProvider)
    }

    @Test
    fun `getPopularMovies should load from JSON`() = runTest {
        // Given
        val jsonContent = """
        {
            "page": 1,
            "results": [
                {"id": 1, "title": "Movie One", "overview": "Overview One", "poster_path": "/poster1.jpg", "release_date": "2024-01-15", "vote_average": 7.5, "vote_count": 1000, "isFavorite": false},
                {"id": 2, "title": "Movie Two", "overview": "Overview Two", "poster_path": "/poster2.jpg", "release_date": "2024-02-20", "vote_average": 8.0, "vote_count": 2000, "isFavorite": false}
            ],
            "total_pages": 1,
            "total_results": 2
        }
        """.trimIndent()
        
        whenever(resourceProvider.openRawResource(R.raw.movies)).thenReturn(jsonContent.byteInputStream())

        // When
        val result = localJsonDataSource.getPopularMovies(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getPopularMovies should return failure on error`() = runTest {
        // Given
        whenever(resourceProvider.openRawResource(R.raw.movies)).thenThrow(RuntimeException("File not found"))

        // When
        val result = localJsonDataSource.getPopularMovies(1)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `searchMovies should filter results by query`() = runTest {
        // Given
        val jsonContent = """
        {
            "page": 1,
            "results": [
                {"id": 1, "title": "Movie One", "overview": "Overview One", "poster_path": "/poster1.jpg", "release_date": "2024-01-15", "vote_average": 7.5, "vote_count": 1000, "isFavorite": false},
                {"id": 2, "title": "Movie Two", "overview": "Overview Two", "poster_path": "/poster2.jpg", "release_date": "2024-02-20", "vote_average": 8.0, "vote_count": 2000, "isFavorite": false}
            ],
            "total_pages": 1,
            "total_results": 2
        }
        """.trimIndent()
        
        whenever(resourceProvider.openRawResource(R.raw.movies)).thenReturn(jsonContent.byteInputStream())

        // When
        val result = localJsonDataSource.searchMovies("One", 1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("Movie One", result.getOrNull()?.get(0)?.title)
    }

    @Test
    fun `getMovieDetails should return movie by id`() = runTest {
        // Given
        val jsonContent = """
        {
            "page": 1,
            "results": [
                {"id": 1, "title": "Movie One", "overview": "Overview One", "poster_path": "/poster1.jpg", "release_date": "2024-01-15", "vote_average": 7.5, "vote_count": 1000, "isFavorite": false},
                {"id": 2, "title": "Movie Two", "overview": "Overview Two", "poster_path": "/poster2.jpg", "release_date": "2024-02-20", "vote_average": 8.0, "vote_count": 2000, "isFavorite": false}
            ],
            "total_pages": 1,
            "total_results": 2
        }
        """.trimIndent()
        
        whenever(resourceProvider.openRawResource(R.raw.movies)).thenReturn(jsonContent.byteInputStream())

        // When
        val result = localJsonDataSource.getMovieDetails(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("Movie One", result.getOrNull()?.title)
    }

    @Test
    fun `getMovieDetails should return failure when not found`() = runTest {
        // Given
        val jsonContent = """
        {
            "page": 1,
            "results": [
                {"id": 1, "title": "Movie One", "overview": "Overview One", "poster_path": "/poster1.jpg", "release_date": "2024-01-15", "vote_average": 7.5, "vote_count": 1000, "isFavorite": false}
            ],
            "total_pages": 1,
            "total_results": 1
        }
        """.trimIndent()
        
        whenever(resourceProvider.openRawResource(R.raw.movies)).thenReturn(jsonContent.byteInputStream())

        // When
        val result = localJsonDataSource.getMovieDetails(999)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `isAvailable should return true`() = runTest {
        assertTrue(localJsonDataSource.isAvailable())
    }
}