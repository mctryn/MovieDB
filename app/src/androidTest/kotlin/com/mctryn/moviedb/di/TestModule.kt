package com.mctryn.moviedb.di

import androidx.lifecycle.SavedStateHandle
import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.repository.MovieRepository
import com.mctryn.moviedb.domain.usecase.GetPopularMoviesUseCase
import com.mctryn.moviedb.domain.usecase.ObserveFavoritesUseCase
import com.mctryn.moviedb.domain.usecase.ToggleFavoriteUseCase
import com.mctryn.moviedb.presentation.list.MovieListViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Test Koin Module for UI Testing.
 * 
 * Provides CONTROLLABLE Flow emissions for integration tests.
 * 
 * ## Usage from tests:
 * 
 * ```kotlin
 * // Setup initial state
 * testRepository.emitPopularMoviesLoading()
 * 
 * // Later, emit success with custom movies
 * testRepository.emitPopularMovies(listOf(Movie(...)))
 * 
 * // Or emit error
 * testRepository.emitPopularMoviesError("Custom error")
 * ```
 */
val testModule = module {
    single<CoroutineDispatcher> { StandardTestDispatcher() }
    single<MovieRepository> { MockRepository() }
    factory<GetPopularMoviesUseCase> { GetPopularMoviesUseCase(get()) }
    factory<ToggleFavoriteUseCase> { ToggleFavoriteUseCase(get()) }
    factory<ObserveFavoritesUseCase> { ObserveFavoritesUseCase(get()) }

    viewModel {
        MovieListViewModel(
            getPopularMoviesUseCase = get(),
            toggleFavoriteUseCase = get(),
            observeFavoritesUseCase = get(),
            savedStateHandle = SavedStateHandle(),
            dispatcher = get()
        )
    }
}