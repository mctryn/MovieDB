package com.mctryn.moviedb.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mctryn.moviedb.domain.model.Movie

/**
 * Room entity representing a Movie in the local database.
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean = false
) {
    /**
     * Convert entity to domain model.
     */
    fun toDomain(): Movie {
        return Movie(
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

    companion object {
        /**
         * Create entity from domain model.
         */
        fun fromDomain(movie: Movie): MovieEntity {
            return MovieEntity(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                voteCount = movie.voteCount,
                isFavorite = movie.isFavorite
            )
        }
    }
}