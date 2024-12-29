package com.example.movie_app_hiza.data.mappers

import com.example.movie_app_hiza.data.local.MovieEntity
import com.example.movie_app_hiza.data.remote.model.Movie
import com.example.movie_app_hiza.domain.models.MovieModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieMapper @Inject constructor() {

    fun toDomainModel(entity: MovieEntity): MovieModel {
        return MovieModel(
            id = entity.id,
            title = entity.title,
            overview = entity.overview,
            releaseDate = entity.releaseDate,
            popularity = entity.popularity,
            posterPath = entity.posterPath,
            isFavorite = entity.isFavorite,
            backdropPath = entity.backdropPath,
            genreIds = entity.genreIds,
            originalLanguage = entity.originalLanguage,
            originalTitle = entity.originalTitle,
            voteAverage = entity.voteAverage,
            voteCount = entity.voteCount,
            video = entity.video,
            adult = entity.adult
        )
    }

    fun toEntity(model: MovieModel): MovieEntity {
        return MovieEntity(
            id = model.id,
            title = model.title,
            overview = model.overview,
            releaseDate = model.releaseDate ?: "",
            popularity = model.popularity ?: 0.0,
            posterPath = model.posterPath ?: "",
            isFavorite = model.isFavorite,
            backdropPath = model.backdropPath ?: "",
            genreIds = model.genreIds,
            originalLanguage = model.originalLanguage,
            originalTitle = model.originalTitle,
            voteAverage = model.voteAverage ?: 0.0,
            voteCount = model.voteCount ?: 0,
            video = model.video,
            adult = model.adult
        )
    }

    fun toDomainModel(movie: Movie): MovieModel {
        return MovieModel(
            id = movie.id,
            title = movie.title,
            overview = movie.overview,
            releaseDate = movie.releaseDate,
            popularity = movie.popularity,
            posterPath = movie.posterPath,
            isFavorite = false,
            backdropPath = movie.backdropPath,
            genreIds = movie.genreIds,
            originalLanguage = movie.originalLanguage,
            originalTitle = movie.originalTitle,
            voteAverage = movie.voteAverage,
            voteCount = movie.voteCount,
            video = movie.video,
            adult = movie.adult
        )
    }
}