package com.mctryn.moviedb.presentation.list

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.usecase.GetPopularMoviesUseCase
import com.mctryn.moviedb.domain.usecase.ToggleFavoriteUseCase
import com.mctryn.moviedb.navigation.NavigationManager
import com.mctryn.moviedb.presentation.list.FavoriteIconState.Checked
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel contract for the Movie List screen.
 */
@Stable
interface IMovieListViewModel {
    val uiState: StateFlow<MovieListUiState>
    fun refresh()
    fun onFavoriteClick(movieId: Int)
    fun onMovieClick(movieId: Int)
}

/**
 * Implementation of MovieListViewModel.
 * Maps RepositoryState to UiState using Flow transformations.
 * 
 * Uses [NavigationManager] for navigation to details screen.
 */
class MovieListViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val navigationManager: NavigationManager,
    private val dispatcher: CoroutineDispatcher
) : ViewModel(), IMovieListViewModel {

    companion object {
        private const val KEY_PAGE = "movie_list_page"
    }

    private val _onFavoriteClickCallback: (Int) -> Unit = { id ->
        viewModelScope.launch {
            toggleFavoriteUseCase(id)
        }
    }
    override val uiState: StateFlow<MovieListUiState> = getPopularMoviesUseCase()
        .toUiState(
            navigationManager = navigationManager,
            onFavoriteClick = _onFavoriteClickCallback,
            refresh = { refresh() }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MovieListUiState.Loading
        )

    override fun refresh() {
        viewModelScope.launch {
            withContext(dispatcher) {
                getPopularMoviesUseCase.refresh()
            }
        }
    }

    override fun onFavoriteClick(movieId: Int) {
        _onFavoriteClickCallback(movieId)
    }

    override fun onMovieClick(movieId: Int) {
        navigationManager.navigateToMovieDetails(movieId)
    }
}

/**
 * Extension function to map RepositoryState to MovieListUiState.
 * Uses [NavigationManager] for navigation to details screen.
 */
private fun Flow<RepositoryState<List<Movie>>>.toUiState(
    navigationManager: NavigationManager,
    onFavoriteClick: (Int) -> Unit,
    refresh: () -> Unit
): Flow<MovieListUiState> = map { repoState ->
    when (repoState) {
        is RepositoryState.Loading -> MovieListUiState.Loading
        is RepositoryState.Error -> MovieListUiState.Error(
            message = repoState.message,
            onRetry = refresh
        )

        is RepositoryState.Success -> {
            val favoriteStates = repoState.data.associate { movie ->
                movie.id to if (movie.isFavorite) {
                    Checked(movie.id) { onFavoriteClick(movie.id) }
                } else {
                    FavoriteIconState.Unchecked(movie.id) { onFavoriteClick(movie.id) }
                }
            }

            MovieListUiState.Content(
                movies = repoState.data,
                onMovieClick = { movieId -> navigationManager.navigateToMovieDetails(movieId) },
                onFavoriteClick = onFavoriteClick,
                favoriteStates = favoriteStates
            )
        }
    }
}