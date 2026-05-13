package com.mctryn.moviedb.di

import androidx.lifecycle.SavedStateHandle
import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.repository.MovieRepository
import com.mctryn.moviedb.domain.usecase.GetPopularMoviesUseCase
import com.mctryn.moviedb.domain.usecase.ToggleFavoriteUseCase
import com.mctryn.moviedb.navigation.NavigationManager
import com.mctryn.moviedb.navigation.TestNavigationManager
import com.mctryn.moviedb.presentation.details.MovieDetailsViewModel
import com.mctryn.moviedb.presentation.list.MovieListViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
 * 
 * // Navigation verification
 * testNavManager.assertNavigatedToMovieId(123)
 * ```
 */
val testModule = module {
    single<CoroutineDispatcher> { Dispatchers.Main.immediate }
    single<MovieRepository> { MockRepository() }
    single<NavigationManager> { TestNavigationManager() }
    factory<GetPopularMoviesUseCase> { GetPopularMoviesUseCase(get()) }
    factory<ToggleFavoriteUseCase> { ToggleFavoriteUseCase(get()) }

    viewModel {
        MovieListViewModel(
            getPopularMoviesUseCase = get(),
            toggleFavoriteUseCase = get(),
            navigationManager = get(),
            dispatcher = get()
        )
    }

    viewModel { (movieId: Int) ->
        MovieDetailsViewModel(
            movieId = movieId,
            savedStateHandle = SavedStateHandle(),
            repository = get(),
            navigationManager = get(),
            dispatcher = get()
        )
    }
}