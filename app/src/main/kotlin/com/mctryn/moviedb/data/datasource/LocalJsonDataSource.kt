package com.mctryn.moviedb.data.datasource

import com.mctryn.moviedb.R
import com.mctryn.moviedb.domain.datasource.MovieDataSource
import com.mctryn.moviedb.domain.model.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Implementation of MovieDataSource that loads movies from local JSON resource.
 * 
 * Responsibility: ONLY read data from JSON file (no caching)
 * SRP: This class has one reason to change - JSON file content.
 */
class LocalJsonDataSource(
    private val resourceProvider: ResourceProvider
) : MovieDataSource {

    private val gson = Gson()

    override fun isAvailable(): Boolean = true

    override suspend fun getPopularMovies(page: Int): Result<List<Movie>> {
        return try {
            val movies = loadMoviesFromJson()
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Result<List<Movie>> {
        return try {
            val movies = loadMoviesFromJson()
            val filtered = movies.filter { movie ->
                movie.title.contains(query, ignoreCase = true)
            }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            val movies = loadMoviesFromJson()
            val movie = movies.find { it.id == movieId }
            if (movie != null) {
                Result.success(movie)
            } else {
                Result.failure(Exception("Movie not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Load movies from local JSON resource file.
     */
    private fun loadMoviesFromJson(): List<Movie> {
        val inputStream = resourceProvider.openRawResource(R.raw.movies)
        val reader = inputStream.reader()
        val type = object : TypeToken<LocalMovieListResponse>() {}.type
        val response: LocalMovieListResponse = gson.fromJson(reader, type)
        reader.close()
        return response.results
    }

    // Local JSON response structure
    private data class LocalMovieListResponse(
        val page: Int,
        val results: List<Movie>,
        val total_pages: Int,
        val total_results: Int
    )
}