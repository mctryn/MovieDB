package com.mctryn.moviedb.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin

@OptIn(KoinExperimentalAPI::class)
val appModules = listOf(
    coroutineModule,
    databaseModule,
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    navigationModule
)

fun initKoin(context: Context) {
    startKoin {
        androidContext(context)
        modules(appModules)
    }
}