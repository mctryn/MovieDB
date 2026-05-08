package com.mctryn.moviedb.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for movie list response from TMDB API
 */
data class MovieListResponse(
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("results")
    val results: List<MovieDto>,
    
    @SerializedName("total_pages")
    val totalPages: Int,
    
    @SerializedName("total_results")
    val totalResults: Int
)

/**
 * DTO for individual movie from TMDB API
 */
data class MovieDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("overview")
    val overview: String,
    
    @SerializedName("poster_path")
    val posterPath: String?,
    
    @SerializedName("release_date")
    val releaseDate: String?,
    
    @SerializedName("vote_average")
    val voteAverage: Double,
    
    @SerializedName("vote_count")
    val voteCount: Int
)

/**
 * DTO for detailed movie response from TMDB API
 */
data class MovieDetailDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("overview")
    val overview: String,
    
    @SerializedName("poster_path")
    val posterPath: String?,
    
    @SerializedName("release_date")
    val releaseDate: String?,
    
    @SerializedName("vote_average")
    val voteAverage: Double,
    
    @SerializedName("vote_count")
    val voteCount: Int,
    
    @SerializedName("runtime")
    val runtime: Int?,
    
    @SerializedName("genres")
    val genres: List<GenreDto>?,
    
    @SerializedName("status")
    val status: String?
)

/**
 * DTO for genre
 */
data class GenreDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String
)