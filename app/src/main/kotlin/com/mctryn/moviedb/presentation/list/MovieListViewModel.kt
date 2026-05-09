package com.mctryn.moviedb.presentation.list

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.usecase.GetPopularMoviesUseCase
import com.mctryn.moviedb.domain.usecase.ObserveFavoritesUseCase
import com.mctryn.moviedb.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.CoroutineDispatcher
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
 */
class MovieListViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val dispatcher: CoroutineDispatcher
) : ViewModel(), IMovieListViewModel {

    companion object {
        private const val KEY_PAGE = "movie_list_page"
    }

    private val _onMovieClickCallback: (Int) -> Unit = { }
    private val _onFavoriteClickCallback: (Int) -> Unit = { id ->
        viewModelScope.launch {
            toggleFavoriteUseCase(id)
        }
    }

    override val uiState: StateFlow<MovieListUiState> = getPopularMoviesUseCase()
        .toUiState(
            onMovieClick = _onMovieClickCallback,
            onFavoriteClick = _onFavoriteClickCallback
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MovieListUiState.Loading
        )

    init {
        observeFavorites()
    }

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
        _onMovieClickCallback(movieId)
    }

    fun setNavigationCallback(callback: (Int) -> Unit) {
        // Update the callback reference if needed
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            observeFavoritesUseCase()
                .collect { favoritesState ->
                    if (favoritesState is RepositoryState.Success) {
                        val currentState = uiState.value
                        if (currentState is MovieListUiState.Content) {
                            val favoriteIds = favoritesState.data.map { it.id }.toSet()
                            val updatedMovies = currentState.movies.map { movie ->
                                movie.copy(isFavorite = favoriteIds.contains(movie.id))
                            }
                        }
                    }
                }
        }
    }
}

/**
 * Extension function to map RepositoryState to MovieListUiState.
 */
private fun kotlinx.coroutines.flow.Flow<RepositoryState<List<com.mctryn.moviedb.domain.model.Movie>>>.toUiState(
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit
): kotlinx.coroutines.flow.Flow<MovieListUiState> = map { repoState ->
    when (repoState) {
        is RepositoryState.Loading -> MovieListUiState.Loading
        is RepositoryState.Error -> MovieListUiState.Error(
            message = repoState.message,
            onRetry = null // Set by ViewModel
        )
        is RepositoryState.Success -> MovieListUiState.Content(
            movies = repoState.data,
            onMovieClick = onMovieClick,
            onFavoriteClick = onFavoriteClick
        )
    }
}