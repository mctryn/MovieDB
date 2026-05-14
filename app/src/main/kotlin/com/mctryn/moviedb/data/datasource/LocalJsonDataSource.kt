package com.mctryn.moviedb.data.datasource

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.mctryn.moviedb.R
import com.mctryn.moviedb.domain.datasource.MovieDataSource
import com.mctryn.moviedb.domain.model.Movie

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

    override suspend fun getPopularMovies(page: Int): Result<List<Movie>> {
        return try {
            val movies = loadMoviesFromJson()
            Result.success(movies)
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
        return response.results.map { it.toDomain() }
    }

    // Local JSON response structure
    private data class LocalMovieListResponse(
        val page: Int,
        val results: List<LocalMovieDto>,
        val total_pages: Int,
        val total_results: Int
    )

    private data class LocalMovieDto(
        val id: Int,
        val title: String,
        val overview: String,
        @SerializedName("poster_path")
        val posterPath: String?,
        @SerializedName("release_date")
        val releaseDate: String?,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int,
        val isFavorite: Boolean = false
    ) {
        fun toDomain(): Movie = Movie(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            releaseDate = releaseDate.orEmpty(),
            voteAverage = voteAverage,
            voteCount = voteCount,
            isFavorite = isFavorite
        )
    }
}
