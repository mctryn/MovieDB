package com.mctryn.moviedb.data.local.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mctryn.moviedb.domain.model.Movie
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).build()
        movieDao = database.movieDao()
    }

    // ===== Insert Tests =====

    @Test
    fun insertMovie_shouldInsertSuccessfully() = runTest {
        val movie = createTestMovieEntity(id = 1, title = "Test Movie")

        movieDao.insertMovie(movie)
        val result = movieDao.getMovieById(1)

        assertNotNull(result)
        assertEquals("Test Movie", result?.title)
    }

    @Test
    fun insertMovies_shouldInsertMultiple() = runTest {
        val movies = listOf(
            createTestMovieEntity(id = 1, title = "Movie 1"),
            createTestMovieEntity(id = 2, title = "Movie 2"),
            createTestMovieEntity(id = 3, title = "Movie 3")
        )

        movieDao.insertMovies(movies)
        val result = movieDao.getAllMovies()

        assertEquals(3, result.size)
    }

    // ===== Get Tests =====

    @Test
    fun getAllMovies_shouldReturnAll() = runTest {
        val movies = listOf(
            createTestMovieEntity(id = 1),
            createTestMovieEntity(id = 2),
            createTestMovieEntity(id = 3)
        )
        movieDao.insertMovies(movies)

        val result = movieDao.getAllMovies()

        assertEquals(3, result.size)
    }

    @Test
    fun getMovieById_shouldReturnMovie() = runTest {
        val movie = createTestMovieEntity(id = 42, title = "Specific Movie")
        movieDao.insertMovie(movie)

        val result = movieDao.getMovieById(42)

        assertNotNull(result)
        assertEquals("Specific Movie", result?.title)
    }

    @Test
    fun getMovieById_notFound_shouldReturnNull() = runTest {
        val result = movieDao.getMovieById(999)

        assertNull(result)
    }

    // ===== Favorites Tests =====

    @Test
    fun getFavorites_shouldReturnOnlyFavorites() = runTest {
        val movies = listOf(
            createTestMovieEntity(id = 1, title = "Fav 1", isFavorite = true),
            createTestMovieEntity(id = 2, title = "Not Fav", isFavorite = false),
            createTestMovieEntity(id = 3, title = "Fav 2", isFavorite = true)
        )
        movieDao.insertMovies(movies)

        val result = movieDao.getFavorites().first()

        assertEquals(2, result.size)
        assertTrue(result.all { it.isFavorite })
    }

    @Test
    fun updateFavorite_shouldToggleFavorite() = runTest {
        val movie = createTestMovieEntity(id = 1, title = "Test", isFavorite = false)
        movieDao.insertMovie(movie)

        movieDao.updateFavorite(1, true)
        val afterUpdate = movieDao.getMovieById(1)

        assertTrue(afterUpdate?.isFavorite == true)
    }

    @Test
    fun updateFavorite_toFalse_shouldRemoveFromFavorites() = runTest {
        val movie = createTestMovieEntity(id = 1, title = "Test", isFavorite = true)
        movieDao.insertMovie(movie)

        movieDao.updateFavorite(1, false)
        val favorites = movieDao.getFavorites().first()

        assertTrue(favorites.isEmpty())
    }

    // ===== Delete Tests =====

    @Test
    fun deleteMovie_shouldRemoveMovie() = runTest {
        val movie = createTestMovieEntity(id = 1)
        movieDao.insertMovie(movie)

        movieDao.deleteMovie(1)
        val result = movieDao.getMovieById(1)

        assertNull(result)
    }

    @Test
    fun deleteAllMovies_shouldClearDatabase() = runTest {
        val movies = listOf(
            createTestMovieEntity(id = 1),
            createTestMovieEntity(id = 2),
            createTestMovieEntity(id = 3)
        )
        movieDao.insertMovies(movies)

        movieDao.deleteAllMovies()
        val result = movieDao.getAllMovies()

        assertTrue(result.isEmpty())
    }

    // ===== Update Tests =====

    @Test
    fun insertDuplicate_shouldUpdate() = runTest {
        val movie1 = createTestMovieEntity(id = 1, title = "Original")
        val movie2 = createTestMovieEntity(id = 1, title = "Updated")

        movieDao.insertMovie(movie1)
        movieDao.insertMovie(movie2)

        val result = movieDao.getMovieById(1)
        assertEquals("Updated", result?.title)
    }

    // ===== Helper =====

    private fun createTestMovieEntity(
        id: Int = 0,
        title: String = "Test Movie",
        overview: String = "Test Overview",
        posterPath: String? = "/poster.jpg",
        releaseDate: String = "2024-01-01",
        voteAverage: Double = 7.5,
        voteCount: Int = 1000,
        isFavorite: Boolean = false
    ): MovieEntity {
        return MovieEntity(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            isFavorite = isFavorite
        )
    }
}