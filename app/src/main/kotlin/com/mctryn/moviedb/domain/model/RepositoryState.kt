package com.mctryn.moviedb.domain.model

/**
 * Represents the state of a repository operation.
 */
sealed interface RepositoryState<out T> {
    /**
     * Initial loading state when data is being fetched.
     */
    data object Loading : RepositoryState<Nothing>

    /**
     * Success state with data.
     */
    data class Success<T>(val data: T) : RepositoryState<T>

    /**
     * Error state with message.
     */
    data class Error(val message: String) : RepositoryState<Nothing>
}