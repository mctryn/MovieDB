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

// ===== DTO to Entity =====

fun MovieDto.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        isFavorite = false
    )
}

fun MovieDetailDto.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        isFavorite = false
    )
}

// ===== Entity to Domain =====

fun MovieEntity.toDomain(): Movie {
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

// ===== Domain to Entity =====

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

// ===== List extensions =====

fun List<MovieDto>.toDomainList(): List<Movie> = map { it.toDomain() }
fun List<MovieDto>.toEntityList(): List<MovieEntity> = map { it.toEntity() }
fun List<MovieEntity>.toDomainEntitiesList(): List<Movie> = map { it.toDomain() }
