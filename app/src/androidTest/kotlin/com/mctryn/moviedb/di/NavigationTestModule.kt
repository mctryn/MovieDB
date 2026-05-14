package com.mctryn.moviedb.di

import com.mctryn.moviedb.domain.repository.MovieRepository
import com.mctryn.moviedb.domain.usecase.GetMovieDetailsUseCase
import com.mctryn.moviedb.domain.usecase.GetPopularMoviesUseCase
import com.mctryn.moviedb.domain.usecase.ObserveFavoritesUseCase
import com.mctryn.moviedb.domain.usecase.RefreshFavoritesUseCase
import com.mctryn.moviedb.domain.usecase.RefreshMovieDetailsUseCase
import com.mctryn.moviedb.domain.usecase.RefreshPopularMoviesUseCase
import com.mctryn.moviedb.domain.usecase.ToggleFavoriteUseCase
import com.mctryn.moviedb.navigation.BackStackNavigationManager
import com.mctryn.moviedb.navigation.NavigationManager
import com.mctryn.moviedb.presentation.details.MovieDetailsViewModel
import com.mctryn.moviedb.presentation.favorites.FavoritesViewModel
import com.mctryn.moviedb.presentation.list.MovieListViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Test Koin module for full navigation integration tests.
 * Uses the real BackStackNavigationManager so MovieDbApp mutates the actual NavDisplay back stack.
 */
val navigationTestModule = module {
    single<CoroutineDispatcher> { Dispatchers.Main.immediate }
    single<MovieRepository> { MockRepository() }
    single<NavigationManager> { BackStackNavigationManager() }
    factory<GetPopularMoviesUseCase> { GetPopularMoviesUseCase(get()) }
    factory<GetMovieDetailsUseCase> { GetMovieDetailsUseCase(get()) }
    factory<ObserveFavoritesUseCase> { ObserveFavoritesUseCase(get()) }
    factory<ToggleFavoriteUseCase> { ToggleFavoriteUseCase(get()) }
    factory<RefreshPopularMoviesUseCase> { RefreshPopularMoviesUseCase(get()) }
    factory<RefreshFavoritesUseCase> { RefreshFavoritesUseCase(get()) }
    factory<RefreshMovieDetailsUseCase> { RefreshMovieDetailsUseCase(get()) }

    viewModel {
        MovieListViewModel(
            getPopularMoviesUseCase = get(),
            toggleFavoriteUseCase = get(),
            navigationManager = get(),
            dispatcher = get(),
            refreshPopularMoviesUseCase = get()
        )
    }

    viewModel {
        FavoritesViewModel(
            observeFavoritesUseCase = get(),
            toggleFavoriteUseCase = get(),
            navigationManager = get(),
            dispatcher = get(),
            refreshFavoritesUseCase = get()
        )
    }

    viewModel { (movieId: Int) ->
        MovieDetailsViewModel(
            movieId = movieId,
            toggleFavoriteUseCase = get(),
            navigationManager = get(),
            dispatcher = get(),
            getMovieDetailsUseCase = get(),
            refreshMovieDetailsUseCase = get()
        )
    }
}
