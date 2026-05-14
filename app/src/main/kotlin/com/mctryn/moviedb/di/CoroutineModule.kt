package com.mctryn.moviedb.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

/**
 * Coroutine module for dispatcher bindings.
 */
val coroutineModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
}
