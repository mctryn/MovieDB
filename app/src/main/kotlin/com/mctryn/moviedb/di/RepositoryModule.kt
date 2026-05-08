package com.mctryn.moviedb.di

import com.mctryn.moviedb.data.datasource.CacheDataSource
import com.mctryn.moviedb.data.datasource.LocalJsonDataSource
import com.mctryn.moviedb.domain.datasource.MovieDataSource
import com.mctryn.moviedb.data.datasource.RemoteDataSource
import com.mctryn.moviedb.data.datasource.ResourceProvider
import com.mctryn.moviedb.data.datasource.ResourceProviderImpl
import com.mctryn.moviedb.data.repository.MovieRepositoryImpl
import com.mctryn.moviedb.domain.repository.MovieRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * DataSource and Repository module for dependency injection.
 * 
 * Data source priority (decided at DI initialization):
 * - Has API key → RemoteDataSource (fetches from API)
 * - No API key → LocalJsonDataSource (reads from JSON)
 * 
 * Both DataSources delegate caching to Repository.
 * CacheDataSource handles all database operations.
 */
val repositoryModule = module {
    // ResourceProvider - provides access to local resources
    single<ResourceProvider> { ResourceProviderImpl(androidContext()) }
    
    // CacheDataSource - handles ALL database operations
    // Used by Repository for caching and favorites
    single { CacheDataSource(get()) }
    
    // MovieDataSource - chosen based on API key availability
    // Repository only knows this interface (dependency inversion)
    single<MovieDataSource> {
        val apiKey: String? = getOrNull()
        
        if (!apiKey.isNullOrBlank()) {
            // Has API key - use remote data source
            RemoteDataSource(apiService = get())
        } else {
            // No API key - use local JSON data source
            LocalJsonDataSource(resourceProvider = get())
        }
    }
    
    // MovieRepository - uses DataSource for fetching, CacheDataSource for DB
    single<MovieRepository> { 
        MovieRepositoryImpl(
            movieDataSource = get(),
            cacheDataSource = get()
        ) 
    }
}