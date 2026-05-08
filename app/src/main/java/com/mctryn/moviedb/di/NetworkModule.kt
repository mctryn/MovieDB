package com.mctryn.moviedb.di

import org.koin.dsl.module

/**
 * Network module for Retrofit and OkHttp configuration.
 * 
 * TODO: Phase 2 - Implement network layer
 * - Create TmdbApi interface
 * - Configure Retrofit with OkHttp
 * - Add logging interceptor
 */
val networkModule = module {
    // TODO: Add network providers
    // single { Retrofit.Builder()...build() }
    // single { get<Retrofit>().create(TmdbApi::class.java) }
}