package com.mctryn.moviedb.data.datasource

import com.mctryn.moviedb.data.mapper.toDomain
import com.mctryn.moviedb.data.mapper.toDomainList
import com.mctryn.moviedb.data.remote.api.MovieApiService
import com.mctryn.moviedb.domain.datasource.MovieDataSource
import com.mctryn.moviedb.domain.model.Movie

/**
 * Remote data source for fetching movies from API.
 * 
 * Responsibility: ONLY fetch data from API (no caching)
 * 
 * SRP: This class has one reason to change - API changes.
 */
class RemoteDataSource(
    private val apiService: MovieApiService
) : MovieDataSource {

    override suspend fun getPopularMovies(page: Int): Result<List<Movie>> {
        return try {
            val response = apiService.getPopularMovies(page = page)
            Result.success(response.results.toDomainList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            val response = apiService.getMovieDetails(movieId = movieId)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}