package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting movie details.
 */
class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    /**
     * Get movie details with state.
     */
    operator fun invoke(movieId: Int): Flow<RepositoryState<Movie>> {
        return repository.getMovieDetails(movieId)
    }
}
