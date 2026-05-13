package com.mctryn.moviedb.presentation.common

import androidx.compose.runtime.Stable
import com.mctryn.moviedb.domain.model.Movie

@Stable
data class MovieItemUiModel(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean
)

fun Movie.toUiModel(): MovieItemUiModel = MovieItemUiModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    isFavorite = isFavorite
)
