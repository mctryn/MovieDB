package com.mctryn.moviedb.domain.usecase

import com.mctryn.moviedb.domain.model.Movie
import com.mctryn.moviedb.domain.model.RepositoryState
import com.mctryn.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting popular movies.
 */
class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    /**
     * Get popular movies with state.
     */
    operator fun invoke(): Flow<RepositoryState<List<Movie>>> {
        return repository.getPopularMovies()
    }
}
