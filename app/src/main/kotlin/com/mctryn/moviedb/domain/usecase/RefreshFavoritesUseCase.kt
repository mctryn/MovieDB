package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.repository.MovieRepository

/**
 * Use case for refreshing favorites source data into local cache.
 *
 * Favorite state is preserved by the repository refresh logic.
 */
class RefreshFavoritesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.refreshPopularMovies()
    }
}
