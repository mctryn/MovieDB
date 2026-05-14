package com.mctryn.moviedb.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * UI States for the favorite icon with animation states.
 * 
 * Pattern: [FavoriteIconState] is a sealed class hierarchy where each state
 * is a singleton data class with a @Composable Show() function.
 * 
 * Usage:
 * ```kotlin
 * // Direct state
 * FavoriteIconState.Checked.Show(onClick = { ... })
 * 
 * // From boolean
 * val state = FavoriteIconState.fromBoolean(movie.isFavorite)
 * state.Show(onClick = { ... })
 * 
 * // Or extension on Boolean
 * movie.isFavorite.toFavoriteState().Show(onClick = { ... })
 * ```
 */
@Immutable
sealed interface FavoriteIconState {

    /**
     * Renders this favorite icon state as a Composable.
     *
     * @param modifier Modifier for the icon button
     */
    @Composable
    fun Show(modifier: Modifier = Modifier)

    /** Not a favorite - shows outline icon */
    data class Unchecked(
        private val movieId: Int,
        private val onClick: () -> Unit
    ) : FavoriteIconState {
        @Composable
        override fun Show(modifier: Modifier) {
            FavoriteIconAnimation(
                targetScale = 1f,
                targetTint = MaterialTheme.colorScheme.onSurfaceVariant,
                icon = Icons.Default.FavoriteBorder,
                contentDescription = "Toggle favorite $movieId",
                onClick = onClick,
                modifier = modifier
            )
        }
    }

    /** Is a favorite - shows filled icon */
    data class Checked(
        private val movieId: Int,
        private val onClick: () -> Unit
    ) : FavoriteIconState {
        @Composable
        override fun Show(modifier: Modifier) {
            FavoriteIconAnimation(
                targetScale = 1f,
                targetTint = Color.Red,
                icon = Icons.Default.Favorite,
                contentDescription = "Untoggle favorite $movieId",
                onClick = onClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun FavoriteIconAnimation(
    targetScale: Float,
    targetTint: Color,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "favorite_scale"
    )

    val tint by animateColorAsState(
        targetValue = targetTint,
        animationSpec = tween(durationMillis = 200),
        label = "favorite_color"
    )

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .semantics { this.contentDescription = contentDescription }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.scale(scale),
            tint = tint
        )
    }
}