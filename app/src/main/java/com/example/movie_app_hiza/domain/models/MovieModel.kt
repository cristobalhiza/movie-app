package com.example.movie_app_hiza.domain.models

data class MovieModel(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String?,
    val popularity: Double?,
    val posterPath: String?,
    val isFavorite: Boolean = false,
    val backdropPath: String?,
    val genreIds: List<Int> = emptyList(),
    val originalLanguage: String,
    val originalTitle: String,
    val voteAverage: Double?,
    val voteCount: Int?,
    val video: Boolean,
    val adult: Boolean
) {
    val formattedYear: String
        get() = if (!releaseDate.isNullOrEmpty() && releaseDate.length >= 4) {
            releaseDate.substring(0, 4)
        } else {
            "Unknown Year"
        }
}


