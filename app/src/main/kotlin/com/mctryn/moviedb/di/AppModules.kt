package com.mctryn.moviedb.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin

@OptIn(KoinExperimentalAPI::class)
val appModules = listOf(
    databaseModule,
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    navigationModule
)

fun initKoin() {
    startKoin {
        modules(appModules)
    }
}