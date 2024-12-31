package com.example.movie_app_hiza.domain.models

data class UserModel(
    val uid: String,
    val email: String,
    val favoriteMovies: List<Int> = emptyList()
)
