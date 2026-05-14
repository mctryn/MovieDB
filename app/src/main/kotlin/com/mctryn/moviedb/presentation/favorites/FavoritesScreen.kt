package com.mctryn.moviedb.presentation.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mctryn.moviedb.R
import com.mctryn.moviedb.presentation.common.BottomNavState
import com.mctryn.moviedb.presentation.common.MovieDbTopAppBar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    navigateToMoviesList: () -> Unit
) {
    val viewModel = koinViewModel<FavoritesViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            MovieDbTopAppBar(
                title = stringResource(R.string.favorites_title)
            )
        },
        bottomBar = {
            BottomNavState.FavoritesSelected.Show(navigateToMoviesList)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            uiState.Show()
        }
    }
}
