package com.example.movie_app_hiza.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        movieDao = database.movieDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveFavoriteMovies() = runBlocking {
        val movie = MovieEntity(
            id = 1,
            adult = false,
            backdropPath = "backdrop_path",
            genreIds = listOf(1, 2, 3),
            originalLanguage = "en",
            originalTitle = "Original Title",
            overview = "Overview",
            popularity = 7.5,
            posterPath = "poster_path",
            releaseDate = "2023-08-01",
            title = "Movie Title",
            video = false,
            voteAverage = 8.0,
            voteCount = 100,
            isFavorite = true
        )

        movieDao.insertMovie(movie)
        val favorites = movieDao.getFavorites().first()

        assertEquals(1, favorites.size)
        assertEquals(movie, favorites[0])
    }

    @Test
    fun updateFavoriteStatus() = runBlocking {
        val movie = MovieEntity(
            id = 1,
            adult = false,
            backdropPath = "backdrop_path",
            genreIds = listOf(1, 2, 3),
            originalLanguage = "en",
            originalTitle = "Original Title",
            overview = "Overview",
            popularity = 7.5,
            posterPath = "poster_path",
            releaseDate = "2023-08-01",
            title = "Movie Title",
            video = false,
            voteAverage = 8.0,
            voteCount = 100,
            isFavorite = false
        )
        movieDao.insertMovie(movie)

        movieDao.updateFavorite(movie.id, true)
        val updatedMovie = movieDao.getFavorites().first()

        assertEquals(1, updatedMovie.size)
        assertEquals(true, updatedMovie[0].isFavorite)
    }

    @Test
    fun getMovieById() = runBlocking {
        val movie = MovieEntity(
            id = 1,
            adult = false,
            backdropPath = "backdrop_path",
            genreIds = listOf(1, 2, 3),
            originalLanguage = "en",
            originalTitle = "Original Title",
            overview = "Overview",
            popularity = 7.5,
            posterPath = "poster_path",
            releaseDate = "2023-08-01",
            title = "Movie Title",
            video = false,
            voteAverage = 8.0,
            voteCount = 100,
            isFavorite = false
        )
        movieDao.insertMovie(movie)

        val result = movieDao.getMovieById(movie.id).first()

        assertEquals(movie, result)
    }
}
