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
    single<ResourceProvider> { ResourceProviderImpl(androidContext()) }
    
    single { CacheDataSource(get()) }
    
    single<MovieDataSource> {
        val apiKey: String? = getOrNull()
        
        if (!apiKey.isNullOrBlank()) {
            RemoteDataSource(apiService = get())
        } else {
            LocalJsonDataSource(resourceProvider = get())
        }
    }
    
    single<MovieRepository> {
        MovieRepositoryImpl(
            movieDataSource = get(),
            cacheDataSource = get()
        ) 
    }
}