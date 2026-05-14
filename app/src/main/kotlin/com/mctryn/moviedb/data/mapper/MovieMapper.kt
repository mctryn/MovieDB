package com.mctryn.moviedb.data.mapper

import com.mctryn.moviedb.data.local.database.MovieEntity
import com.mctryn.moviedb.data.remote.dto.MovieDetailDto
import com.mctryn.moviedb.data.remote.dto.MovieDto
import com.mctryn.moviedb.domain.model.Movie

/**
 * Mapper functions for converting between data layer models and domain models.
 * 
 * Provides centralized mapping logic to keep data classes clean.
 */

// ===== DTO to Domain =====

fun MovieDto.toDomain(isFavorite: Boolean = false): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        isFavorite = isFavorite
    )
}

fun MovieDetailDto.toDomain(isFavorite: Boolean = false): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        isFavorite = isFavorite
    )
}

fun Movie.toEntity(): MovieEntity {
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

fun List<MovieDto>.toDomainList(): List<Movie> = map { it.toDomain() }
fun List<MovieEntity>.toDomainEntitiesList(): List<Movie> = map { it.toDomain() }
