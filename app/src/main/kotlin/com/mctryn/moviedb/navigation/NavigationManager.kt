package com.mctryn.moviedb.navigation

interface NavigationManager {
    fun navigateToMovieDetails(movieId: Int)
    fun navigateBack()
    fun canNavigateBack(): Boolean
}