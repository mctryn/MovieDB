package com.mctryn.moviedb.domain.repository

import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.model.RepositoryState
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Movie data operations.
 */
interface MovieRepository {
    
    /**
     * Get popular movies with state.
     * Emits Loading → Success(Error) states.
     */
    fun getPopularMovies(): Flow<RepositoryState<List<Movie>>>
    
    /**
     * Refresh popular movies from API and update Room.
     */
    suspend fun refreshPopularMovies(): Result<Unit>
    
    /**
     * Get movie details with state.
     * Emits Loading → Success(Error) states.
     */
    fun getMovieDetails(movieId: Int): Flow<RepositoryState<Movie>>
    
    /**
     * Get favorites with state.
     */
    fun getFavorites(): Flow<RepositoryState<List<Movie>>>
    
    /**
     * Toggle favorite status for a movie.
     */
    suspend fun toggleFavorite(movieId: Int): Result<Unit>
}