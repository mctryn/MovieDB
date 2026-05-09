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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mctryn.moviedb.domain.model.Movie

/**
 * Sealed interface representing UI states for Movie List screen.
 * 
 * Each state implements Show() to provide its Composable UI.
 * This follows the Sealed Interface + Show() pattern from the template.
 */
@Stable
sealed interface MovieListUiState {
    
    /**
     * Loading state - displays loading indicator.
     */
    @Stable
    data object Loading : MovieListUiState {
        @Composable
        override fun Show() {
            LoadingPane(message = "Loading movies...")
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
            ErrorPane(message = message, onRetry = onRetry)
        }
    }
    
    /**
     * Content state - displays list of movies.
     */
    @Stable
    data class Content(
        val movies: List<Movie>,
        val onMovieClick: (Int) -> Unit,
        val onFavoriteClick: (Int) -> Unit
    ) : MovieListUiState {
        @Composable
        override fun Show() {
            MovieListContent(
                movies = movies,
                onMovieClick = onMovieClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
    
    /**
     * Abstract function that returns a Composable for this state.
     * Each concrete state implements this to provide its UI representation.
     */
    @Composable
    fun Show()
}

// ============================================================================
// Reusable Components
// ============================================================================

/**
 * Loading pane - displays a loading indicator with message.
 */
@Composable
fun LoadingPane(
    modifier: Modifier = Modifier,
    message: String = "Loading..."
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Error pane - displays error message with optional retry button.
 */
@Composable
fun ErrorPane(
    modifier: Modifier = Modifier,
    message: String,
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

/**
 * Movie list content - displays list of movies.
 */
@Composable
fun MovieListContent(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (movies.isEmpty()) {
        EmptyPane(message = "No movies found")
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
                    onFavoriteClick = { onFavoriteClick(movie.id) }
                )
            }
        }
    }
}

/**
 * Empty pane - displays message when no data available.
 */
@Composable
fun EmptyPane(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Movie card - displays individual movie item.
 */
@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: Movie,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Poster placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = movie.title.take(1),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            
            // Movie details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⭐ ${movie.voteAverage} (${movie.voteCount})",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}