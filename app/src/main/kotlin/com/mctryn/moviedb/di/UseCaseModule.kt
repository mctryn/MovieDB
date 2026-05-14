package com.mctryn.moviedb.di

import com.mctryn.moviedb.domain.usecase.GetMovieDetailsUseCase
import com.mctryn.moviedb.domain.usecase.GetPopularMoviesUseCase
import com.mctryn.moviedb.domain.usecase.ObserveFavoritesUseCase
import com.mctryn.moviedb.domain.usecase.RefreshFavoritesUseCase
import com.mctryn.moviedb.domain.usecase.RefreshMovieDetailsUseCase
import com.mctryn.moviedb.domain.usecase.RefreshPopularMoviesUseCase
import com.mctryn.moviedb.domain.usecase.ToggleFavoriteUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Use Case module for dependency injection.
 * 
 * Provides use cases from the domain layer.
 */
val useCaseModule = module {
    singleOf(::GetPopularMoviesUseCase)
    singleOf(::RefreshPopularMoviesUseCase)
    singleOf(::GetMovieDetailsUseCase)
    singleOf(::RefreshMovieDetailsUseCase)
    singleOf(::ToggleFavoriteUseCase)
    singleOf(::ObserveFavoritesUseCase)
    singleOf(::RefreshFavoritesUseCase)
}
