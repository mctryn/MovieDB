package com.mctryn.moviedb.presentation.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.mctryn.moviedb.R
import com.mctryn.moviedb.presentation.common.MovieDbTopAppBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: Int
) {
    val viewModel: MovieDetailsViewModel = koinViewModel(
        key = movieId.toString(),
        parameters = { parametersOf(movieId) }
    )
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            MovieDbTopAppBar(
                title = stringResource(R.string.details_title),
                navigationIcon = {
                    IconButton(
                        onClick = viewModel::onBack,
                        modifier = Modifier.semantics { contentDescription = "Back" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        state.Show(
            modifier = Modifier.padding(innerPadding),
            onRetry = viewModel::refresh,
            onToggleFavorite = viewModel::onToggleFavorite
        )
    }
}