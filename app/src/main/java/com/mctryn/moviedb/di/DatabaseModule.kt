package com.mctryn.moviedb.di

import org.koin.dsl.module

/**
 * Database module for Room database configuration.
 * 
 * TODO: Phase 2 - Implement database and DAOs
 * - Create MovieDatabase
 * - Create MovieDao
 * - Configure Room with Koin
 */
val databaseModule = module {
    // TODO: Add database providers
    // single { Room.databaseBuilder(...) }
    // single { get<MovieDatabase>().movieDao() }
}