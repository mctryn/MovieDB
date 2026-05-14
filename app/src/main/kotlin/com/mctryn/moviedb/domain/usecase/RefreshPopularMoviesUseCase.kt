package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.repository.MovieRepository

/**
 * Use case for refreshing popular movies from datasource into local cache.
 */
class RefreshPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.refreshPopularMovies()
    }
}
