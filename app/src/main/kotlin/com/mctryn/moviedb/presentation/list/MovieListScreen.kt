package com.mctryn.moviedb.presentation.list

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

/**
 * Movie List Screen.
 * 
 * Displays a list of popular movies with support for:
 * - Loading state
 * - Error state with retry
 * - Content state with movie list
 * - Pull-to-refresh
 * - Favorite toggle
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    onNavigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<MovieListViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.setNavigationCallback(onNavigateToDetails)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Movie Browser") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
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