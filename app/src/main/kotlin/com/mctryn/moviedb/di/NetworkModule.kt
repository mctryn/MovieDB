package com.mctryn.moviedb.di

import com.mctryn.moviedb.BuildConfig
import com.mctryn.moviedb.data.remote.api.MovieApiService
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TMDB_BASE_URL = "https://api.themoviedb.org/"

/**
 * Network module for Retrofit and OkHttp configuration.
 */
val networkModule = module {
    single<String?> {
        BuildConfig.TMDB_API_KEY
            .takeIf { it.isNotBlank() && it != "YOUR_TMDB_API_KEY" }
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    single {
        Interceptor { chain ->
            val originalRequest = chain.request()
            val apiKey: String? = getOrNull()

            val updatedUrl = originalRequest.url.newBuilder().apply {
                if (!apiKey.isNullOrBlank()) {
                    addQueryParameter("api_key", apiKey)
                }
            }.build()

            val updatedRequest = originalRequest.newBuilder()
                .url(updatedUrl)
                .build()

            chain.proceed(updatedRequest)
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL.toHttpUrl())
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<MovieApiService> {
        get<Retrofit>().create(MovieApiService::class.java)
    }
}
