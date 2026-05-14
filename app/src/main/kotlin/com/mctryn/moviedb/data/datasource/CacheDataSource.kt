package com.mctryn.moviedb.data.datasource

import com.mctryn.moviedb.data.local.database.MovieDao
import com.mctryn.moviedb.data.mapper.toDomainEntitiesList
import com.mctryn.moviedb.data.mapper.toEntity
import com.mctryn.moviedb.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Cache data source for database operations.
 * 
 * Responsibilities:
 * 1. Generic caching (store movies from API/JSON)
 * 2. Favorites management (CRUD operations)
 * 3. Provides reactive Flow-based access to movies
 * 
 * This is the single source of truth for all database operations.
 */
class CacheDataSource(private val movieDao: MovieDao) {

    // ===== Generic Caching (Reactive) =====

    /**
     * Get all movies as a Flow (reactive).
     * Emits updated list whenever database changes.
     */
    fun getMoviesFlow(): Flow<List<Movie>> {
        return movieDao.getAllMoviesFlow().map { entities ->
            entities.toDomainEntitiesList()
        }
    }

    /**
     * Get all movies as a list (one-time fetch).
     */
    suspend fun getMovies(): List<Movie> {
        return movieDao.getAllMovies().toDomainEntitiesList()
    }

    /**
     * Get a movie by its ID as a Flow.
     */
    fun getMovieByIdFlow(movieId: Int): Flow<Movie?> {
        return movieDao.getMovieByIdFlow(movieId).map { entity ->
            entity?.toDomain()
        }
    }

    /**
     * Get a movie by its ID.
     */
    suspend fun getMovieById(movieId: Int): Movie? {
        return movieDao.getMovieById(movieId)?.toDomain()
    }

    /**
     * Save movies transactionally (clears and inserts fresh).
     */
    suspend fun saveMoviesTransactional(movies: List<Movie>) {
        movieDao.insertMoviesTransactional(movies.map { it.toEntity() })
    }

    /**
     * Save movies to database.
     */
    suspend fun saveMovies(movies: List<Movie>) {
        movieDao.insertMovies(movies.map { it.toEntity() })
    }

    /**
     * Save a single movie to database.
     */
    suspend fun saveMovie(movie: Movie) {
        movieDao.insertMovie(movie.toEntity())
    }

    // ===== Favorites Management =====

    /**
     * Get favorites as a Flow (reactive).
     */
    fun getFavorites(): Flow<List<Movie>> {
        return movieDao.getFavorites().map { entities ->
            entities.toDomainEntitiesList()
        }
    }

    /**
     * Update the favorite status of a movie.
     */
    suspend fun updateFavorite(movieId: Int, isFavorite: Boolean) {
        movieDao.updateFavorite(movieId, isFavorite)
    }

    /**
     * Delete a movie by its ID.
     */
    suspend fun deleteMovie(movieId: Int) {
        movieDao.deleteMovie(movieId)
    }

    /**
     * Clear all movies from database.
     */
    suspend fun clearAll() {
        movieDao.deleteAllMovies()
    }
}