package com.mctryn.moviedb.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.usecase.GetMovieDetailsUseCase
import com.mctryn.moviedb.domain.usecase.RefreshMovieDetailsUseCase
import com.mctryn.moviedb.domain.usecase.ToggleFavoriteUseCase
import com.mctryn.moviedb.navigation.NavigationManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieId: Int,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val refreshMovieDetailsUseCase: RefreshMovieDetailsUseCase,
    private val navigationManager: NavigationManager,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()
    private var detailsJob: Job? = null

    init {
        if (movieId <= 0) {
            _uiState.value = MovieDetailsUiState.Error("Invalid movie id")
        } else {
            loadMovieDetails()
            viewModelScope.launch(dispatcher) {
                refreshMovieDetailsUseCase(movieId)
                    .onFailure {
                        _uiState.value =
                            MovieDetailsUiState.Error(it.message ?: "Failed to fetch movie details")
                    }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch(dispatcher) {
            _uiState.value = MovieDetailsUiState.Loading
            refreshMovieDetailsUseCase(movieId)
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
            toggleFavoriteUseCase.invoke(movieId)
        }
    }

    private fun loadMovieDetails() {
        detailsJob?.cancel()
        detailsJob = viewModelScope.launch(dispatcher) {
            getMovieDetailsUseCase(movieId).collect { state ->
                _uiState.value = when (state) {
                    is RepositoryState.Loading -> MovieDetailsUiState.Loading
                    is RepositoryState.Success -> MovieDetailsUiState.Content(state.data)
                    is RepositoryState.Error -> MovieDetailsUiState.Error(state.message)
                }
            }
        }
    }
}