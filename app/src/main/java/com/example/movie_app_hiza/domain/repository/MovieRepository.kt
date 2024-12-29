package com.example.movie_app_hiza.domain.repository

import com.example.movie_app_hiza.data.local.MovieDao
import com.example.movie_app_hiza.data.mappers.MovieMapper
import com.example.movie_app_hiza.data.remote.MovieApi
import com.example.movie_app_hiza.domain.models.MovieModel
import com.example.movie_app_hiza.presentation.viewmodel.MoviesUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val movieApi: MovieApi,
    private val movieMapper: MovieMapper
) {

    fun getPopularMovies(pages: Int): Flow<MoviesUiState> = flow {
        emit(MoviesUiState(isLoading = true))
        val allMovies = mutableListOf<MovieModel>()
        try {
            for (page in 1..pages) {
                val response = movieApi.getPopularMovies(page)
                val movies = response.movies.map { movieMapper.toDomainModel(it) }
                allMovies.addAll(movies)
            }
            emit(MoviesUiState(movies = allMovies, isLoading = false))
        } catch (e: Exception) {
            emit(MoviesUiState(isLoading = false, error = e.message))
        }
    }

    suspend fun searchMovies(query: String, page: Int): Result<List<MovieModel>> {
        return try {
            val response = movieApi.searchMovies(query, page)
            Result.success(response.movies.map { movieMapper.toDomainModel(it) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getFavoriteMovies(): Flow<List<MovieModel>> {
        return movieDao.getFavorites().map { entities ->
            entities.map { movieMapper.toDomainModel(it) }
        }
    }

    suspend fun addMovieToFavorites(movie: MovieModel) {
        val entity = movieMapper.toEntity(movie)
        movieDao.insertMovie(entity)
        movieDao.updateFavorite(movie.id, true)
    }

    suspend fun removeMovieFromFavorites(movieId: Int) {
        movieDao.updateFavorite(movieId, false)
    }

    fun getMovieById(id: Int): Flow<MovieModel?> {
        return movieDao.getMovieById(id).map { entity ->
            entity?.let { movieMapper.toDomainModel(it) }
        }
    }

    suspend fun getMovieDetailsApi(id: Int): MovieModel? {
        return try {
            val movie = movieApi.getMovieById(id)
            movieMapper.toDomainModel(movie)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

