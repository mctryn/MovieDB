package com.mctryn.moviedb.data.remote.api

import com.mctryn.moviedb.data.remote.dto.MovieDetailDto
import com.mctryn.moviedb.data.remote.dto.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for TMDB API
 */
interface MovieApiService {

    /**
     * Get popular movies
     * @param page Page number for pagination
     * @param language Language code (default: en-US)
     */
    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieListResponse

    /**
     * Search movies by query
     * @param query Search query string
     * @param page Page number for pagination
     * @param language Language code (default: en-US)
     */
    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieListResponse

    /**
     * Get movie details by ID
     * @param movieId The movie ID
     * @param language Language code (default: en-US)
     */
    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieDetailDto
}