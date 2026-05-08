package com.mctryn.moviedb.domain.model

/**
 * Domain model representing a Movie.
 * 
 * This is the core entity used throughout the application.
 */
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean = false
)