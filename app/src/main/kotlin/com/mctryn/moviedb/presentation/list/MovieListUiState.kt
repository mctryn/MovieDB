package com.mctryn.moviedb.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource
import com.mctryn.moviedb.R
import com.mctryn.moviedb.presentation.common.MovieItemUiModel
import com.mctryn.moviedb.presentation.common.ErrorPane
import com.mctryn.moviedb.presentation.common.LoadingPane
import com.mctryn.moviedb.presentation.common.MovieListContent

/**
 * Sealed interface representing UI states for Movie List screen.
 * 
 * Each state implements Show() to provide its Composable UI.
 * This follows the Sealed Interface + Show() pattern from the template.
 */
@Stable
sealed interface MovieListUiState {


    /**
     * Abstract function that returns a Composable for this state.
     * Each concrete state implements this to provide its UI representation.
     */
    @Composable
    fun Show()

    /**
     * Loading state - displays loading indicator.
     */
    @Stable
    data object Loading : MovieListUiState {
        @Composable
        override fun Show() {
            LoadingPane(message = stringResource(R.string.loading_movies))
        }
    }

    /**
     * Error state - displays error message with optional retry.
     */
    @Stable
    data class Error(
        val message: String,
        val onRetry: (() -> Unit)? = null
    ) : MovieListUiState {
        @Composable
        override fun Show() {
            ErrorPane(
                message = message,
                onRetry = onRetry
            )
        }
    }

    /**
     * Content state - displays list of movies.
     */
    @Stable
    data class Content(
        val movies: List<MovieItemUiModel>,
        val onMovieClick: (Int) -> Unit,
        val onFavoriteClick: (Int) -> Unit,
    ) : MovieListUiState {
        @Composable
        override fun Show() {
            MovieListContent(
                movies = movies,
                onMovieClick = onMovieClick,
                onFavoriteClick = onFavoriteClick,
            )
        }
    }

}