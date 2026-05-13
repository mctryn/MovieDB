package com.mctryn.moviedb.di

import com.mctryn.moviedb.presentation.details.MovieDetailsViewModel
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
    viewModel { (movieId: Int) ->
        MovieDetailsViewModel(
            movieId = movieId,
            savedStateHandle = get(),
            repository = get(),
            navigationManager = get(),
            dispatcher = get()
        )
    }
}