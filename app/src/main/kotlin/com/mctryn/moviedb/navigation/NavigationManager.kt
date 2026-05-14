package com.mctryn.moviedb.navigation

interface NavigationManager {
    fun navigateToMovieDetails(movieId: Int)
    fun navigateToMovieList()
    fun navigateToFavorites()
    fun navigateBack()
    fun canNavigateBack(): Boolean
}
