package com.mctryn.moviedb.di

import androidx.navigation3.runtime.entryProvider
import com.mctryn.moviedb.navigation.AppEntryProvider
import com.mctryn.moviedb.navigation.BackStackNavigationManager
import com.mctryn.moviedb.navigation.FavoritesNav
import com.mctryn.moviedb.navigation.MovieDetailsNav
import com.mctryn.moviedb.navigation.MovieListNav
import com.mctryn.moviedb.navigation.NavigationManager
import com.mctryn.moviedb.navigation.movieDetailsNavigationTransitions
import com.mctryn.moviedb.presentation.details.MovieDetailsScreen
import com.mctryn.moviedb.presentation.favorites.FavoritesScreen
import com.mctryn.moviedb.presentation.list.MovieListScreen
import org.koin.dsl.module

val navigationModule = module {
    single<NavigationManager> { BackStackNavigationManager() }
    single<AppEntryProvider> {
        entryProvider {
            entry<MovieListNav> {
                val navigationManager: NavigationManager = get()
                MovieListScreen(navigateToFavoriteList = { navigationManager.navigateToFavorites() })
            }
            entry<FavoritesNav> {
                val navigationManager: NavigationManager = get()
                FavoritesScreen(navigateToMoviesList = { navigationManager.navigateToMovieList() })
            }
            entry<MovieDetailsNav>(
                metadata = { movieDetailsNavigationTransitions() }
            ) { key ->
                MovieDetailsScreen(movieId = key.movieId)
            }
        }
    }
}
