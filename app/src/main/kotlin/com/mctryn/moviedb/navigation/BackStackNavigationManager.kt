package com.mctryn.moviedb.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class BackStackNavigationManager : NavigationManager {

    private var backStack: NavBackStack<NavKey>? = null

    fun attachBackStack(backStack: NavBackStack<NavKey>) {
        this.backStack = backStack
    }

    override fun navigateToMovieDetails(movieId: Int) {
        backStack?.add(MovieDetailsNav(movieId))
    }

    override fun navigateToMovieList() {
        backStack?.run {
            clear()
            add(MovieListNav)
        }
    }

    override fun navigateToFavorites() {
        backStack?.run {
            clear()
            add(FavoritesNav)
        }
    }

    override fun navigateBack() {
        if (canNavigateBack()) {
            backStack?.removeLastOrNull()
        }
    }

    override fun canNavigateBack(): Boolean = (backStack?.size ?: 0) > 1
}
