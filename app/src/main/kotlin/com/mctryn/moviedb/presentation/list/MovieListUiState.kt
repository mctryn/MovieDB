package com.mctryn.moviedb.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.mctryn.moviedb.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.presentation.list.components.EmptyPane
import com.mctryn.moviedb.presentation.list.components.ErrorPane
import com.mctryn.moviedb.presentation.list.components.LoadingPane
import com.mctryn.moviedb.presentation.list.components.MovieCard
import com.mctryn.moviedb.presentation.list.components.MovieListContent

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
        val movies: List<Movie>,
        val onMovieClick: (Int) -> Unit,
        val onFavoriteClick: (Int) -> Unit,
        val favoriteStates: Map<Int, FavoriteIconState> = emptyMap()
    ) : MovieListUiState {
        @Composable
        override fun Show() {
            MovieListContent(
                movies = movies,
                onMovieClick = onMovieClick,
                onFavoriteClick = onFavoriteClick,
                favoriteStates = favoriteStates
            )
        }
    }

}