package com.mctryn.moviedb.data.repository

import com.mctryn.moviedb.data.datasource.CacheDataSource
import com.mctryn.moviedb.domain.datasource.MovieDataSource
import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Implementation of MovieRepository.
 * Provides reactive Flow-based access with state emissions.
 */
class MovieRepositoryImpl(
    private val movieDataSource: MovieDataSource,
    private val cacheDataSource: CacheDataSource
) : MovieRepository {

    // ===== Popular Movies =====

    override fun getPopularMovies(): Flow<RepositoryState<List<Movie>>> = flow {
        emit(RepositoryState.Loading)
        
        val cached = cacheDataSource.getMovies()
        
        if (cached.isEmpty()) {
            val result = movieDataSource.getPopularMovies(page = 1)
            result.fold(
                onSuccess = { movies ->
                    cacheDataSource.saveMoviesTransactional(movies)
                    cacheDataSource.getMoviesFlow().collect { emit(RepositoryState.Success(it)) }
                },
                onFailure = { error ->
                    emit(RepositoryState.Error(error.message ?: "Failed to fetch movies"))
                }
            )
        } else {
            cacheDataSource.getMoviesFlow().collect { emit(RepositoryState.Success(it)) }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun refreshPopularMovies(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val result = movieDataSource.getPopularMovies(page = 1)
                result.getOrNull()?.let { movies ->
                    cacheDataSource.saveMoviesTransactional(movies)
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // ===== Movie Details =====

    override fun getMovieDetails(movieId: Int): Flow<RepositoryState<Movie>> = flow {
        emit(RepositoryState.Loading)
        
        val cached = cacheDataSource.getMovieById(movieId)
        
        if (cached == null) {
            val result = movieDataSource.getMovieDetails(movieId)
            result.fold(
                onSuccess = { movie ->
                    cacheDataSource.saveMovie(movie)
                    // Emit the saved movie (non-null)
                    emit(RepositoryState.Success(movie))
                },
                onFailure = { error ->
                    emit(RepositoryState.Error(error.message ?: "Failed to fetch movie details"))
                }
            )
        } else {
            // Emit cached movie directly
            emit(RepositoryState.Success(cached))
        }
    }.flowOn(Dispatchers.IO)

    // ===== Favorites =====

    override fun getFavorites(): Flow<RepositoryState<List<Movie>>> = flow {
        emit(RepositoryState.Loading)
        cacheDataSource.getFavorites().collect { emit(RepositoryState.Success(it)) }
    }.flowOn(Dispatchers.IO)

    override suspend fun toggleFavorite(movieId: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val existing = cacheDataSource.getMovieById(movieId)

                if (existing != null) {
                    cacheDataSource.updateFavorite(movieId, !existing.isFavorite)
                } else {
                    val movieResult = movieDataSource.getMovieDetails(movieId)
                    movieResult.getOrNull()?.let { movie ->
                        cacheDataSource.saveMovie(movie.copy(isFavorite = true))
                    } ?: return@withContext Result.failure(
                        movieResult.exceptionOrNull() ?: Exception("Movie not found")
                    )
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}