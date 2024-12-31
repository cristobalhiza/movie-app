package com.example.movie_app_hiza.presentation.viewmodel

import com.example.movie_app_hiza.domain.models.MovieModel

data class MoviesUiState(
    val movies: List<MovieModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0,
    val totalPages: Int = 1
)
