package com.mctryn.moviedb.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.repository.MovieRepository
import com.mctryn.moviedb.navigation.NavigationManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    movieId: Int,
    private val savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
    private val navigationManager: NavigationManager,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        const val MOVIE_ID_KEY = "movieId"
    }

    private val movieId: Int = savedStateHandle.get<Int>(MOVIE_ID_KEY) ?: movieId
    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()
    private var detailsJob: Job? = null

    init {
        savedStateHandle[MOVIE_ID_KEY] = this.movieId

        if (this.movieId <= 0) {
            _uiState.value = MovieDetailsUiState.Error("Invalid movie id")
        } else {
            loadMovieDetails()
            viewModelScope.launch(dispatcher) {
                repository.refreshMovieDetails(this@MovieDetailsViewModel.movieId)
                    .onFailure {
                        _uiState.value =
                            MovieDetailsUiState.Error(it.message ?: "Failed to fetch movie details")
                    }
            }
        }
    }

    fun onRetry() {
        viewModelScope.launch(dispatcher) {
            _uiState.value = MovieDetailsUiState.Loading
            repository.refreshMovieDetails(movieId)
                .onFailure {
                    _uiState.value =
                        MovieDetailsUiState.Error(it.message ?: "Failed to fetch movie details")
                }
        }
    }

    fun onBack() {
        navigationManager.navigateBack()
    }

    fun onToggleFavorite() {
        viewModelScope.launch(dispatcher) {
            repository.toggleFavorite(movieId)
        }
    }

    private fun loadMovieDetails() {
        detailsJob?.cancel()
        detailsJob = viewModelScope.launch(dispatcher) {
            repository.getMovieDetails(movieId).collect { state ->
                _uiState.value = when (state) {
                    is RepositoryState.Loading -> MovieDetailsUiState.Loading
                    is RepositoryState.Success -> MovieDetailsUiState.Content(state.data)
                    is RepositoryState.Error -> MovieDetailsUiState.Error(state.message)
                }
            }
        }
    }
}