package com.mctryn.moviedb.presentation.list

import androidx.compose.runtime.Stable

/**
 * User actions available on the Movie List screen.
 * Uses Stable for stable referential equality in Compose.
 * Follows the Base + Empty pattern from the template.
 */
@Stable
interface MovieListScreenActions {
    /** Refresh the movie list */
    fun refresh()
    
    /** Navigate to movie details */
    fun onMovieClick(movieId: Int)
    
    /** Toggle favorite status */
    fun onFavoriteClick(movieId: Int)
    
    /**
     * Base implementation delegating to ViewModel.
     * Use for production screens.
     */
    @Stable
    class Base(private val viewModel: IMovieListViewModel) : MovieListScreenActions {
        override fun refresh() = viewModel.refresh()
        override fun onMovieClick(movieId: Int) = viewModel.onMovieClick(movieId)
        override fun onFavoriteClick(movieId: Int) = viewModel.onFavoriteClick(movieId)
    }
    
    /**
     * Empty implementation for previews and testing.
     */
    @Stable
    data object Empty : MovieListScreenActions {
        override fun refresh() {}
        override fun onMovieClick(movieId: Int) {}
        override fun onFavoriteClick(movieId: Int) {}
    }
}