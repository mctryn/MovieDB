package com.mctryn.moviedb.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database for Movie application.
 */
@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    /**
     * Get the MovieDao for database operations.
     */
    abstract fun movieDao(): MovieDao

    companion object {
        const val DATABASE_NAME = "moviedb_database"
    }
}