package com.mctryn.moviedb.di

import com.mctryn.moviedb.presentation.details.MovieDetailsViewModel
import com.mctryn.moviedb.presentation.favorites.FavoritesViewModel
import com.mctryn.moviedb.presentation.list.MovieListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * ViewModel module for dependency injection.
 * 
 * Provides ViewModels for the presentation layer.
 */
val viewModelModule = module {
    viewModelOf(::MovieListViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModel { (movieId: Int) ->
        MovieDetailsViewModel(
            movieId = movieId,
            toggleFavoriteUseCase = get(),
            getMovieDetailsUseCase = get(),
            refreshMovieDetailsUseCase = get(),
            navigationManager = get(),
            dispatcher = get()
        )
    }
}
