package com.mctryn.moviedb.domain.datasource

import com.mctryn.moviedb.domain.model.Movie

/**
 * Interface for movie data sources.
 * 
 * This interface is defined in the domain layer to abstract
 * data source implementation details from the business logic.
 * 
 * Clean Architecture: Domain layer defines this interface,
 * Data layer provides implementations (RemoteDataSource, LocalJsonDataSource).
 */
interface MovieDataSource {
    
    /**
     * Get popular movies.
     * 
     * @param page Page number for pagination
     * @return Result containing list of movies or error
     */
    suspend fun getPopularMovies(page: Int = 1): Result<List<Movie>>
    
    /**
     * Search movies by query.
     * 
     * @param query Search query string
     * @param page Page number for pagination
     * @return Result containing list of movies or error
     */
    suspend fun searchMovies(query: String, page: Int = 1): Result<List<Movie>>
    
    /**
     * Get movie details by ID.
     * 
     * @param movieId Movie ID
     * @return Result containing movie or error
     */
    suspend fun getMovieDetails(movieId: Int): Result<Movie>
    
    /**
     * Check if this data source is available.
     * For example, RemoteDataSource might not be available if no API key.
     */
    fun isAvailable(): Boolean
}