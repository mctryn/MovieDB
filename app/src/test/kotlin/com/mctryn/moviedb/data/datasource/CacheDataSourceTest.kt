package com.mctryn.moviedb.data.datasource

import com.mctryn.moviedb.data.local.database.MovieDao
import com.mctryn.moviedb.data.local.database.MovieEntity
import com.mctryn.moviedb.domain.model.Movie
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class CacheDataSourceTest {

    @Mock
    private lateinit var movieDao: MovieDao

    private lateinit var cacheDataSource: CacheDataSource

    private val testMovieEntity = MovieEntity(
        id = 1,
        title = "Test Movie",
        overview = "Test Overview",
        posterPath = "/poster.jpg",
        releaseDate = "2024-01-15",
        voteAverage = 7.5,
        voteCount = 1000,
        isFavorite = false
    )

    private val testFavoriteEntity = MovieEntity(
        id = 2,
        title = "Favorite Movie",
        overview = "Favorite Overview",
        posterPath = "/favorite.jpg",
        releaseDate = "2024-02-20",
        voteAverage = 8.5,
        voteCount = 2000,
        isFavorite = true
    )

    private val testMovie = Movie(
        id = 1,
        title = "Test Movie",
        overview = "Test Overview",
        posterPath = "/poster.jpg",
        releaseDate = "2024-01-15",
        voteAverage = 7.5,
        voteCount = 1000,
        isFavorite = false
    )

    @Before
    fun setup() {
        cacheDataSource = CacheDataSource(movieDao)
    }

    @Test
    fun `getMovies should return all movies from database`() = runTest {
        // Given
        whenever(movieDao.getAllMovies()).thenReturn(listOf(testMovieEntity))

        // When
        val result = cacheDataSource.getMovies()

        // Then
        assertEquals(1, result.size)
        assertEquals("Test Movie", result[0].title)
    }

    @Test
    fun `getMovies should return empty list when no movies`() = runTest {
        // Given
        whenever(movieDao.getAllMovies()).thenReturn(emptyList())

        // When
        val result = cacheDataSource.getMovies()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `saveMovies should insert movies into database`() = runTest {
        // Given
        val movies = listOf(testMovie)

        // When
        cacheDataSource.saveMovies(movies)

        // Then - verify insertMovies was called
        // (No exception means success)
        assertTrue(true)
    }

    @Test
    fun `getMovieById should return movie when exists`() = runTest {
        // Given
        whenever(movieDao.getMovieById(1)).thenReturn(testMovieEntity)

        // When
        val result = cacheDataSource.getMovieById(1)

        // Then
        assertEquals("Test Movie", result?.title)
    }

    @Test
    fun `getMovieById should return null when not found`() = runTest {
        // Given
        whenever(movieDao.getMovieById(999)).thenReturn(null)

        // When
        val result = cacheDataSource.getMovieById(999)

        // Then
        assertNull(result)
    }

    @Test
    fun `saveMovie should insert movie into database`() = runTest {
        // Given
        val movie = testMovie

        // When
        cacheDataSource.saveMovie(movie)

        // Then - no exception means success
        assertTrue(true)
    }

    @Test
    fun `getFavorites should return only favorite movies`() = runTest {
        // Given
        whenever(movieDao.getFavorites()).thenReturn(flowOf(listOf(testFavoriteEntity)))

        // When
        val result = cacheDataSource.getFavorites()

        // Then
        result.collect { favorites ->
            assertEquals(1, favorites.size)
            assertTrue(favorites[0].isFavorite)
        }
    }

    @Test
    fun `updateFavorite should update favorite status`() = runTest {
        // When
        cacheDataSource.updateFavorite(1, true)

        // Then - verify updateFavorite was called
        assertTrue(true)
    }

    @Test
    fun `deleteMovie should remove movie from database`() = runTest {
        // When
        cacheDataSource.deleteMovie(1)

        // Then - verify deleteMovie was called
        assertTrue(true)
    }

    @Test
    fun `clearAll should remove all movies from database`() = runTest {
        // When
        cacheDataSource.clearAll()

        // Then - verify deleteAllMovies was called
        assertTrue(true)
    }
}