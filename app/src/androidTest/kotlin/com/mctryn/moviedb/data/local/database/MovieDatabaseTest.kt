package com.mctryn.moviedb.data.local.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDatabaseTest {

    private lateinit var database: MovieDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).build()
    }

    @Test
    fun database_shouldBeCreated() {
        assertNotNull(database)
    }

    @Test
    fun dao_shouldNotBeNull() {
        val dao = database.movieDao()
        assertNotNull(dao)
    }

    @Test
    fun database_shouldClose() {
        database.close()
        // If we reach here without exception, test passes
        assertTrue(true)
    }
}