package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.repository.MovieRepository

/**
 * Use case for toggling movie favorite status.
 * 
 * Responsibility: Single responsibility - toggle favorite status in repository.
 * 
 * @param repository The movie repository for data access
 */
class ToggleFavoriteUseCase(
    private val repository: MovieRepository
) {
    /**
     * Toggle the favorite status of a movie.
     * 
     * @param movieId The movie ID to toggle
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(movieId: Int): Result<Unit> {
        return repository.toggleFavorite(movieId)
    }
}