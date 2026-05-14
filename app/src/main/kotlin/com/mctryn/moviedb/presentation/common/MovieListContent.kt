package com.mctryn.moviedb.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mctryn.moviedb.R
import kotlinx.collections.immutable.*


/**
 * Movie list content - displays list of movies.
 */
@Composable
fun MovieListContent(
    modifier: Modifier = Modifier,
    movies: ImmutableList<MovieItemUiModel>,
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
) {
    if (movies.isEmpty()) {
        EmptyPane(message = stringResource(R.string.empty_movies))
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = movies,
                key = { it.id }
            ) { movie ->
                MovieCard(
                    movie = movie,
                    onClick = { onMovieClick(movie.id) },
                    favoriteState = if (movie.isFavorite) {
                        FavoriteIconState.Checked(movie.id) { onFavoriteClick(movie.id) }
                    } else {
                        FavoriteIconState.Unchecked(movie.id) { onFavoriteClick(movie.id) }
                    }
                )
            }
        }
    }
}