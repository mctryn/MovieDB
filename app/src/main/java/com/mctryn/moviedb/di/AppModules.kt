package com.mctryn.moviedb.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module

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
    org.koin.core.context.startKoin {
        modules(appModules)
    }
}