package com.mctryn.moviedb.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mctryn.moviedb.R
import com.mctryn.moviedb.domain.model.Movie

sealed interface MovieDetailsUiState {

    @Composable
    fun Show(
        modifier: Modifier = Modifier,
        onRetry: () -> Unit = {},
        onToggleFavorite: () -> Unit = {}
    )

    data object Loading : MovieDetailsUiState {
        @Composable
        override fun Show(
            modifier: Modifier,
            onRetry: () -> Unit,
            onToggleFavorite: () -> Unit
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.details_loading),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    data class Error(val message: String) : MovieDetailsUiState {
        @Composable
        override fun Show(
            modifier: Modifier,
            onRetry: () -> Unit,
            onToggleFavorite: () -> Unit
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = onRetry, modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = stringResource(R.string.retry),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    data class Content(val movie: Movie) : MovieDetailsUiState {
        @Composable
        override fun Show(
            modifier: Modifier,
            onRetry: () -> Unit,
            onToggleFavorite: () -> Unit
        ) {
            val favoriteContentDescription =
                if (movie.isFavorite) "Untoggle favorite ${movie.id}" else "Toggle favorite ${movie.id}"

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.details_poster_placeholder),
                    modifier = Modifier.semantics { contentDescription = "Movie poster" },
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(text = movie.title, style = MaterialTheme.typography.headlineSmall)
                Text(text = movie.overview, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = stringResource(R.string.details_release_date, movie.releaseDate),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.details_rating, movie.voteAverage),
                    style = MaterialTheme.typography.bodySmall
                )

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.semantics {
                        contentDescription = favoriteContentDescription
                    }
                ) {
                    if (movie.isFavorite) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    } else {
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = null)
                    }
                }
            }
        }
    }
}