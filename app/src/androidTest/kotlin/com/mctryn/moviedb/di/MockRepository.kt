package com.mctryn.moviedb.di

import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class MockRepository : MovieRepository {

    private val popularMoviesFlow = MutableSharedFlow<RepositoryState<List<Movie>>>(
        replay = 1,
        extraBufferCapacity = 1
    )

    private val movieDetailsFlows = mutableMapOf<Int, MutableSharedFlow<RepositoryState<Movie>>>()
    private var refreshMovieDetailsResult: Result<Unit> = Result.success(Unit)
    private val refreshMovieDetailsCalls = mutableListOf<Int>()

    // getFavorites() - uses SharedFlow for multiple emissions
    private val favoritesFlow = MutableSharedFlow<RepositoryState<List<Movie>>>(
        replay = 1,
        extraBufferCapacity = 1
    )

    // ═══════════════════════════════════════════════════════════════════
    // Mock Repository with manual emission control
    // ═══════════════════════════════════════════════════════════════════

    // ─────────────────────────────────────────────────────────────────
    // getPopularMovies() - Manual Emission API
    // ─────────────────────────────────────────────────────────────────

    override fun getPopularMovies(): Flow<RepositoryState<List<Movie>>> =
        popularMoviesFlow

    /**
     * Emit Loading state for popular movies.
     * Call this to start the loading indicator.
     */
    fun emitPopularMoviesLoading() {
        popularMoviesFlow.tryEmit(RepositoryState.Loading)
    }

    /**
     * Emit Success with a custom list of movies.
     * Call this to populate the movie list.
     */
    fun emitPopularMovies(movies: List<Movie>) {
        popularMoviesFlow.tryEmit(RepositoryState.Success(movies))
    }

    /**
     * Emit Error for popular movies.
     * Call this to show error state.
     */
    fun emitPopularMoviesError(message: String) {
        popularMoviesFlow.tryEmit(RepositoryState.Error(message))
    }

    // ─────────────────────────────────────────────────────────────────
    // refreshPopularMovies() - Manual Result Control
    // ─────────────────────────────────────────────────────────────────

    private var _refreshResult: Result<Unit> = Result.success(Unit)
    private val refreshCalls = mutableListOf<Unit>()

    override suspend fun refreshPopularMovies(): Result<Unit> {
        refreshCalls.add(Unit)
        return _refreshResult
    }

    fun assertRefreshCalled() {
        if (refreshCalls.isEmpty()) {
            throw AssertionError("refreshPopularMovies() was not called")
        }
    }

    /**
     * Set the result for the next refreshPopularMovies() call.
     * @param success If true, returns success; if false, returns failure with custom message.
     * @param errorMessage Custom error message (used when success = false).
     */
    fun setRefreshResult(success: Boolean, errorMessage: String = "Refresh failed") {
        _refreshResult = if (success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(errorMessage))
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // getMovieDetails(movieId) - Manual Emission API
    // ─────────────────────────────────────────────────────────────────

    override fun getMovieDetails(movieId: Int): Flow<RepositoryState<Movie>> {
        return movieDetailsFlows.getOrPut(movieId) {
            MutableSharedFlow(
                replay = 1,
                extraBufferCapacity = 1
            )
        }
    }

    override suspend fun refreshMovieDetails(movieId: Int): Result<Unit> {
        refreshMovieDetailsCalls.add(movieId)
        return refreshMovieDetailsResult
    }

    /**
     * Emit Loading state for a specific movie's details.
     */
    fun emitMovieDetailsLoading(movieId: Int) {
        getOrCreateDetailsFlow(movieId).tryEmit(RepositoryState.Loading)
    }

    /**
     * Emit Success with a specific movie.
     */
    fun emitMovieDetails(movie: Movie) {
        getOrCreateDetailsFlow(movie.id).tryEmit(RepositoryState.Success(movie))
    }

    /**
     * Emit Error for a specific movie's details.
     */
    fun emitMovieDetailsError(movieId: Int, message: String) {
        getOrCreateDetailsFlow(movieId).tryEmit(RepositoryState.Error(message))
    }

    private fun getOrCreateDetailsFlow(movieId: Int): MutableSharedFlow<RepositoryState<Movie>> {
        return movieDetailsFlows.getOrPut(movieId) {
            MutableSharedFlow(
                replay = 1,
                extraBufferCapacity = 1
            )
        }
    }

    fun assertRefreshMovieDetailsCalled(movieId: Int) {
        if (movieId !in refreshMovieDetailsCalls) {
            throw AssertionError("refreshMovieDetails($movieId) was not called. Calls: $refreshMovieDetailsCalls")
        }
    }

    fun setRefreshMovieDetailsResult(success: Boolean, errorMessage: String = "Refresh details failed") {
        refreshMovieDetailsResult = if (success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(errorMessage))
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // getFavorites() - Manual Emission API
    // ─────────────────────────────────────────────────────────────────

    override fun getFavorites(): Flow<RepositoryState<List<Movie>>> =
        favoritesFlow

    /**
     * Emit Loading state for favorites.
     */
    fun emitFavoritesLoading() {
        favoritesFlow.tryEmit(RepositoryState.Loading)
    }

    /**
     * Emit Success with a list of favorite movies.
     */
    fun emitFavorites(movies: List<Movie>) {
        favoritesFlow.tryEmit(RepositoryState.Success(movies))
    }

    /**
     * Emit Error for favorites.
     */
    fun emitFavoritesError(message: String) {
        favoritesFlow.tryEmit(RepositoryState.Error(message))
    }

    // ─────────────────────────────────────────────────────────────────
    // toggleFavorite() - Manual Result Control
    // ─────────────────────────────────────────────────────────────────

    private var _toggleFavoriteResult: Result<Unit> = Result.success(Unit)
    private val toggleFavoriteCalls = mutableListOf<Int>()

    override suspend fun toggleFavorite(movieId: Int): Result<Unit> {
        toggleFavoriteCalls.add(movieId)
        return _toggleFavoriteResult
    }

    fun assertToggleFavoriteCalled(movieId: Int) {
        if (movieId !in toggleFavoriteCalls) {
            throw AssertionError("toggleFavorite($movieId) was not called. Calls: $toggleFavoriteCalls")
        }
    }

    fun resetToggleFavoriteCalls() {
        toggleFavoriteCalls.clear()
    }

    /**
     * Set the result for the next toggleFavorite() call.
     */
    fun setToggleFavoriteResult(success: Boolean, errorMessage: String = "Toggle failed") {
        _toggleFavoriteResult = if (success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(errorMessage))
        }
    }
}