package com.mctryn.moviedb

import android.app.Application
import com.mctryn.moviedb.di.initKoin

class MovieDbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin DI
        initKoin()
    }
}