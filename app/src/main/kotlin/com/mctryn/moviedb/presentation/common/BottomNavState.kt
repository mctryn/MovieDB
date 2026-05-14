package com.mctryn.moviedb.presentation.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mctryn.moviedb.R

sealed interface BottomNavState {

    @Composable
    fun Show(
        onNavigate: () -> Unit,
        modifier: Modifier = Modifier
    )

    /** Movie List is selected */
    data object MovieListSelected : BottomNavState {
        @Composable
        override fun Show(
            onNavigate: () -> Unit,
            modifier: Modifier
        ) {
            NavigationBar(modifier = modifier) {
                MovieListItem(
                    selected = true,
                    onClick = { /* Already selected */ }
                )
                FavoritesItem(
                    selected = false,
                    onClick = { onNavigate() }

                )
            }
        }
    }

    /** Favorites is selected */
    data object FavoritesSelected : BottomNavState {
        @Composable
        override fun Show(
            onNavigate: () -> Unit,
            modifier: Modifier
        ) {
            NavigationBar(modifier = modifier) {
                MovieListItem(
                    selected = false,
                    onClick = { onNavigate() }
                )
                FavoritesItem(
                    selected = true,
                    onClick = { /* Already selected */ }
                )
            }
        }
    }

    companion object {
        @Composable
        private fun RowScope.MovieListItem(
            selected: Boolean,
            onClick: () -> Unit
        ) {
            NavigationBarItem(
                selected = selected,
                onClick = onClick,
                icon = { Icon(Icons.Default.Movie, contentDescription = null) },
                label = {
                    Text(
                        text = stringResource(R.string.movie_list_tab_label),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }

        @Composable
        private fun RowScope.FavoritesItem(
            selected: Boolean,
            onClick: () -> Unit
        ) {
            NavigationBarItem(
                selected = selected,
                onClick = onClick,
                icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                label = {
                    Text(
                        text = stringResource(R.string.favorites_tab_label),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}