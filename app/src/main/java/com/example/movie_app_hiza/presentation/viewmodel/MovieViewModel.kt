package com.example.movie_app_hiza.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_app_hiza.domain.models.MovieModel
import com.example.movie_app_hiza.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private var currentPage = 1
    private val _popularMoviesState = MutableStateFlow(MoviesUiState())
    val popularMoviesState: StateFlow<MoviesUiState> = _popularMoviesState.asStateFlow()

    private val _favoritesState = MutableStateFlow<List<MovieModel>>(emptyList())
    val favoritesState: StateFlow<List<MovieModel>> = _favoritesState.asStateFlow()

    private val _movieDetailsState = MutableStateFlow<MovieModel?>(null)
    val movieDetailsState: StateFlow<MovieModel?> = _movieDetailsState.asStateFlow()

    var hasMorePages: Boolean = true

    fun fetchPopularMovies() {
        viewModelScope.launch {
            repository.getPopularMovies(currentPage).collect { uiState ->
                if (uiState.error == null) {
                    _popularMoviesState.value = _popularMoviesState.value.copy(
                        movies = _popularMoviesState.value.movies + uiState.movies,
                        isLoading = uiState.isLoading,
                        error = null
                    )
                    hasMorePages = uiState.currentPage < uiState.totalPages
                    currentPage = uiState.currentPage + 1
                } else {
                    _popularMoviesState.value = _popularMoviesState.value.copy(
                        isLoading = false,
                        error = uiState.error
                    )
                }
            }
        }
    }

    fun fetchFavoriteMovies() {
        viewModelScope.launch {
            repository.getFavoriteMovies().collect { movies ->
                _favoritesState.value = movies
            }
        }
    }

    fun searchMovies(query: String, page: Int) {
        viewModelScope.launch {
            val result = repository.searchMovies(query, page)
            if (result.isSuccess) {
                _popularMoviesState.value = MoviesUiState(movies = result.getOrDefault(emptyList()))
            } else {
                _popularMoviesState.value = MoviesUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun addMovieToFavorites(movie: MovieModel) {
        viewModelScope.launch {
            repository.addMovieToFavorites(movie)
            fetchFavoriteMovies()
        }
    }

    fun removeMovieFromFavorites(movieId: Int) {
        viewModelScope.launch {
            repository.removeMovieFromFavorites(movieId)
            fetchFavoriteMovies()
        }
    }

    fun fetchMovieDetails(id: Int) {
        viewModelScope.launch {
            try {
                repository.getMovieById(id).collect { movie ->
                    _movieDetailsState.value = movie ?: repository.getMovieDetailsApi(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
