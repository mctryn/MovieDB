package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.repository.MovieRepository

/**
 * Use case for refreshing movie details from datasource into local cache.
 */
class RefreshMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<Unit> {
        return repository.refreshMovieDetails(movieId)
    }
}
