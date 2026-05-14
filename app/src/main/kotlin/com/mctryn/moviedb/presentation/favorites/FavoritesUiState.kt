package com.mctryn.moviedb.presentation.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource
import com.mctryn.moviedb.R
import com.mctryn.moviedb.presentation.common.MovieItemUiModel
import com.mctryn.moviedb.presentation.common.EmptyPane
import com.mctryn.moviedb.presentation.common.ErrorPane
import com.mctryn.moviedb.presentation.common.LoadingPane
import com.mctryn.moviedb.presentation.common.MovieListContent
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed interface FavoritesUiState {

    @Composable
    fun Show()

    @Stable
    data object Loading : FavoritesUiState {
        @Composable
        override fun Show() {
            LoadingPane(message = stringResource(R.string.favorites_loading))
        }
    }

    @Stable
    data class Error(
        val message: String,
        val onRetry: (() -> Unit)? = null
    ) : FavoritesUiState {
        @Composable
        override fun Show() {
            ErrorPane(
                message = message,
                onRetry = onRetry
            )
        }
    }

    @Stable
    data object Empty : FavoritesUiState {
        @Composable
        override fun Show() {
            EmptyPane(message = stringResource(R.string.favorites_empty))
        }
    }

    @Stable
    data class Content(
        val movies: ImmutableList<MovieItemUiModel>,
        val onMovieClick: (Int) -> Unit,
        val onFavoriteClick: (Int) -> Unit,
    ) : FavoritesUiState {
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
