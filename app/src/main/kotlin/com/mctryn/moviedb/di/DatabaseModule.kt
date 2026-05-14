package com.mctryn.moviedb.di

import androidx.room.Room
import com.mctryn.moviedb.data.local.database.MovieDao
import com.mctryn.moviedb.data.local.database.MovieDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Database module for Room database configuration.
 */
val databaseModule = module {
    single<MovieDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java,
            MovieDatabase.DATABASE_NAME
        ).build()
    }

    single<MovieDao> {
        get<MovieDatabase>().movieDao()
    }
}
