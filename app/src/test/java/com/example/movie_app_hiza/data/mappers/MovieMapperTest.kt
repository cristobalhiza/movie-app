package com.example.movie_app_hiza.data.mappers

import com.example.movie_app_hiza.data.local.MovieEntity
import com.example.movie_app_hiza.data.remote.model.Movie
import com.example.movie_app_hiza.domain.models.MovieModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MovieMapperTest {

    private lateinit var mapper: MovieMapper

    @Before
    fun setUp() {
        mapper = MovieMapper()
    }

    @Test
    fun `toDomainModel maps MovieEntity to MovieModel correctly`() {
        // Given
        val entity = MovieEntity(
            id = 1,
            title = "Movie Title",
            overview = "Movie Overview",
            adult = false,
            backdropPath = "backdrop_path",
            genreIds = listOf(1, 2, 3),
            originalLanguage = "en",
            originalTitle = "Original Title",
            releaseDate = "2023-08-01",
            popularity = 7.5,
            posterPath = "poster_path",
            video = false,
            voteAverage = 8.0,
            voteCount = 100,
            isFavorite = true
        )

        // When
        val result = mapper.toDomainModel(entity)

        // Then
        Assert.assertEquals(entity.id, result.id)
        Assert.assertEquals(entity.title, result.title)
        Assert.assertEquals(entity.overview, result.overview)
        Assert.assertEquals(entity.adult, result.adult)
        Assert.assertEquals(entity.backdropPath, result.backdropPath)
        Assert.assertEquals(entity.genreIds, result.genreIds)
        Assert.assertEquals(entity.originalLanguage, result.originalLanguage)
        Assert.assertEquals(entity.originalTitle, result.originalTitle)
        Assert.assertEquals(entity.releaseDate, result.releaseDate)
        Assert.assertEquals(entity.popularity, result.popularity!!, 0.0)
        Assert.assertEquals(entity.posterPath, result.posterPath)
        Assert.assertEquals(entity.video, result.video)
        Assert.assertEquals(entity.voteAverage, result.voteAverage!!, 0.0)
        Assert.assertEquals(entity.voteCount, result.voteCount)
        Assert.assertEquals(entity.isFavorite, result.isFavorite)
    }

    @Test
    fun `toEntity maps MovieModel to MovieEntity correctly`() {
        // Given
        val model = MovieModel(
            id = 1,
            title = "Movie Title",
            overview = "Movie Overview",
            releaseDate = "2023-08-01",
            popularity = 7.5,
            posterPath = "poster_path",
            isFavorite = true,
            backdropPath = "backdrop_path",
            genreIds = listOf(1, 2, 3),
            originalLanguage = "en",
            originalTitle = "Original Title",
            voteAverage = 8.0,
            voteCount = 100,
            video = false,
            adult = false
        )

        // When
        val result = mapper.toEntity(model)

        // Then
        Assert.assertEquals(model.id, result.id)
        Assert.assertEquals(model.title, result.title)
        Assert.assertEquals(model.overview, result.overview)
        Assert.assertEquals(model.releaseDate, result.releaseDate)
        Assert.assertEquals(model.popularity!!, result.popularity, 0.0)
        Assert.assertEquals(model.posterPath, result.posterPath)
        Assert.assertEquals(model.isFavorite, result.isFavorite)
        Assert.assertEquals(model.backdropPath, result.backdropPath)
        Assert.assertEquals(model.genreIds, result.genreIds)
        Assert.assertEquals(model.originalLanguage, result.originalLanguage)
        Assert.assertEquals(model.originalTitle, result.originalTitle)
        Assert.assertEquals(model.voteAverage!!, result.voteAverage, 0.0)
        Assert.assertEquals(model.voteCount, result.voteCount)
        Assert.assertEquals(model.video, result.video)
        Assert.assertEquals(model.adult, result.adult)
    }

    @Test
    fun `toDomainModel maps Movie to MovieModel correctly`() {
        // Given
        val movie = Movie(
            id = 1,
            title = "Movie Title",
            overview = "Movie Overview",
            releaseDate = "2023-08-01",
            popularity = 7.5,
            posterPath = "poster_path",
            backdropPath = "backdrop_path",
            genreIds = listOf(1, 2, 3),
            originalLanguage = "en",
            originalTitle = "Original Title",
            voteAverage = 8.0,
            voteCount = 100,
            video = false,
            adult = false
        )

        // When
        val result = mapper.toDomainModel(movie)

        // Then
        Assert.assertEquals(movie.id, result.id)
        Assert.assertEquals(movie.title, result.title)
        Assert.assertEquals(movie.overview, result.overview)
        Assert.assertEquals(movie.releaseDate, result.releaseDate)
        Assert.assertEquals(movie.popularity, result.popularity)
        Assert.assertEquals(movie.posterPath, result.posterPath)
        Assert.assertEquals(movie.backdropPath, result.backdropPath)
        Assert.assertEquals(movie.genreIds, result.genreIds)
        Assert.assertEquals(movie.originalLanguage, result.originalLanguage)
        Assert.assertEquals(movie.originalTitle, result.originalTitle)
        Assert.assertEquals(movie.voteAverage, result.voteAverage)
        Assert.assertEquals(movie.voteCount, result.voteCount)
        Assert.assertEquals(movie.video, result.video)
        Assert.assertEquals(movie.adult, result.adult)
        Assert.assertFalse(result.isFavorite)
    }
}