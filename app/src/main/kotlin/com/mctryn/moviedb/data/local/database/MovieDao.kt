package com.mctryn.moviedb.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Movie entities.
 */
@Dao
interface MovieDao {

    /**
     * Insert a single movie.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    /**
     * Insert multiple movies transactionally.
     * Clears existing data before inserting new data.
     */
    @Transaction
    suspend fun insertMoviesTransactional(movies: List<MovieEntity>) {
        deleteAllMovies()
        if (movies.isNotEmpty()) {
            insertMovies(movies)
        }
    }

    /**
     * Insert multiple movies.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    /**
     * Get all movies as a Flow (reactive).
     */
    @Query("SELECT * FROM movies ORDER BY id ASC")
    fun getAllMoviesFlow(): Flow<List<MovieEntity>>

    /**
     * Get all movies from the database.
     */
    @Query("SELECT * FROM movies ORDER BY id ASC")
    suspend fun getAllMovies(): List<MovieEntity>

    /**
     * Get a movie by its ID as a Flow.
     */
    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieByIdFlow(movieId: Int): Flow<MovieEntity?>

    /**
     * Get a movie by its ID.
     */
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    /**
     * Get all favorite movies as a Flow.
     */
    @Query("SELECT * FROM movies WHERE isFavorite = 1 ORDER BY id ASC")
    fun getFavorites(): Flow<List<MovieEntity>>

    /**
     * Update the favorite status of a movie.
     */
    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun updateFavorite(movieId: Int, isFavorite: Boolean)

    /**
     * Delete a movie by its ID.
     */
    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovie(movieId: Int)

    /**
     * Delete all movies from the database.
     */
    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}