package com.mctryn.moviedb.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object MovieListNav : NavKey

@Serializable
data object FavoritesNav : NavKey

@Serializable
data class MovieDetailsNav(val movieId: Int) : NavKey
