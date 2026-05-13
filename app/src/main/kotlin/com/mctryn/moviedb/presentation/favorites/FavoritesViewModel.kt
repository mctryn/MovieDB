package com.mctryn.moviedb.presentation.favorites

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.usecase.ObserveFavoritesUseCase
import com.mctryn.moviedb.domain.usecase.ToggleFavoriteUseCase
import com.mctryn.moviedb.navigation.NavigationManager
import com.mctryn.moviedb.presentation.common.toUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Stable
interface IFavoritesViewModel {
    val uiState: StateFlow<FavoritesUiState>
    fun onMovieClick(movieId: Int)
    fun onFavoriteClick(movieId: Int)
    fun retry()
}

class FavoritesViewModel(
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val navigationManager: NavigationManager,
    private val dispatcher: CoroutineDispatcher
) : ViewModel(), IFavoritesViewModel {

    private val onFavoriteClickCallback: (Int) -> Unit = { id ->
        viewModelScope.launch {
            toggleFavoriteUseCase(id)
        }
    }

    override val uiState: StateFlow<FavoritesUiState> = observeFavoritesUseCase()
        .toUiState(
            navigationManager = navigationManager,
            onFavoriteClick = onFavoriteClickCallback,
            retry = { retry() }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoritesUiState.Loading
        )

    override fun onMovieClick(movieId: Int) {
        navigationManager.navigateToMovieDetails(movieId)
    }

    override fun onFavoriteClick(movieId: Int) {
        onFavoriteClickCallback(movieId)
    }

    override fun retry() {
        viewModelScope.launch {
            withContext(dispatcher) {
                observeFavoritesUseCase()
            }
        }
    }
}

private fun Flow<RepositoryState<List<Movie>>>.toUiState(
    navigationManager: NavigationManager,
    onFavoriteClick: (Int) -> Unit,
    retry: () -> Unit
): Flow<FavoritesUiState> = map { repoState ->
    when (repoState) {
        is RepositoryState.Loading -> FavoritesUiState.Loading
        is RepositoryState.Error -> FavoritesUiState.Error(
            message = repoState.message,
            onRetry = retry
        )

        is RepositoryState.Success -> {
            if (repoState.data.isEmpty()) {
                FavoritesUiState.Empty
            } else {
                FavoritesUiState.Content(
                    movies = repoState.data.map { it.toUiModel() },
                    onMovieClick = { movieId -> navigationManager.navigateToMovieDetails(movieId) },
                    onFavoriteClick = onFavoriteClick,
                )
            }
        }
    }
}
