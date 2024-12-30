package com.example.movie_app_hiza.domain.repository

import com.example.movie_app_hiza.data.local.MovieDao
import com.example.movie_app_hiza.data.local.MovieEntity
import com.example.movie_app_hiza.data.mappers.MovieMapper
import com.example.movie_app_hiza.data.remote.MovieApi
import com.example.movie_app_hiza.data.remote.model.Movie
import com.example.movie_app_hiza.domain.models.MovieModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MovieRepositoryTest {

    private lateinit var movieMapper: MovieMapper
    private lateinit var movieRepository: MovieRepository
    @MockK
    private lateinit var movieApi: MovieApi
    @MockK
    private lateinit var movieDao: MovieDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        movieMapper = MovieMapper()
        movieRepository = MovieRepository(movieDao, movieApi, movieMapper)
    }

    @Test
    fun `getFavoriteMovies returns mapped movies from dao`() = runTest {
        val movieEntity = MovieEntity(
            id = 1,
            title = "Movie 1",
            overview = "Overview",
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
        val movieModel = movieMapper.toDomainModel(movieEntity)

        coEvery { movieDao.getFavorites() } returns flowOf(listOf(movieEntity))

        val result = movieRepository.getFavoriteMovies()

        result.collect { favorites ->
            assertEquals(1, favorites.size)
            assertEquals(movieModel, favorites.first())
        }

        coVerify { movieDao.getFavorites() }
    }

    @Test
    fun `removeMovieFromFavorites updates favorite status in dao`() = runTest {
        val movieId = 1
        coEvery { movieDao.updateFavorite(movieId, false) } returns 1

        movieRepository.removeMovieFromFavorites(movieId)

        coVerify { movieDao.updateFavorite(movieId, false) }
    }

    @Test
    fun `getMovieById returns mapped movie from dao`() = runTest {
        val movieId = 1
        val movieEntity = MovieEntity(
            id = 1,
            title = "Movie 1",
            overview = "Overview",
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
        val movieModel = movieMapper.toDomainModel(movieEntity)

        coEvery { movieDao.getMovieById(movieId) } returns flowOf(movieEntity)

        val result = movieRepository.getMovieById(movieId)


        result.collect { movie ->
            assertEquals(movieModel, movie)
        }

        coVerify { movieDao.getMovieById(movieId) }
    }

    @Test
    fun `searchMovies fetches and maps movies from api`() = runTest {
        val query = "Inception"
        val page = 1
        val movieRemote = Movie(
            id = 1,
            title = "Inception",
            overview = "A mind-bending thriller",
            releaseDate = "2010-07-15",
            popularity = 100.0,
            posterPath = "/poster_path.jpg",
            backdropPath = "/backdrop_path.jpg",
            genreIds = listOf(1, 2, 3),
            originalLanguage = "en",
            originalTitle = "Inception",
            voteAverage = 8.5,
            voteCount = 10000,
            video = false,
            adult = false
        )
        val movieModel = movieMapper.toDomainModel(movieRemote)

        coEvery { movieApi.searchMovies(query, page) } returns mockk {
            every { movies } returns listOf(movieRemote)
        }

        val result = movieRepository.searchMovies(query, page)
        assertEquals(Result.success(listOf(movieModel)), result)

        coVerify { movieApi.searchMovies(query, page) }
    }

    @Test
    fun `getPopularMovies fetches and maps movies from api`() = runTest {
        val page = 1
        val movieRemote = Movie(
            id = 1,
            title = "Popular Movie",
            overview = "A popular movie",
            releaseDate = "2023-01-01",
            popularity = 10.0,
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(1, 2),
            originalLanguage = "en",
            originalTitle = "Popular Original",
            voteAverage = 7.5,
            voteCount = 200,
            video = false,
            adult = false
        )
        val movieModel = movieMapper.toDomainModel(movieRemote)

        coEvery { movieApi.getPopularMovies(page) } returns mockk {
            every { movies } returns listOf(movieRemote)
        }

        val result = movieRepository.getPopularMovies(1)

        result.collect { uiState ->
            if (!uiState.isLoading) {
                assertEquals(listOf(movieModel), uiState.movies)
            }
        }

        coVerify { movieApi.getPopularMovies(page) }
    }

    @Test
    fun `getMovieDetailsApi fetches and maps movie details`() = runTest {
        val movieRemote = Movie(
            id = 1,
            title = "Detailed Movie",
            overview = "Detailed overview",
            releaseDate = "2020-12-12",
            popularity = 15.0,
            posterPath = "/poster_detail.jpg",
            backdropPath = "/backdrop_detail.jpg",
            genreIds = listOf(2, 3),
            originalLanguage = "en",
            originalTitle = "Detailed Original",
            voteAverage = 8.0,
            voteCount = 500,
            video = false,
            adult = false
        )
        val movieModel = movieMapper.toDomainModel(movieRemote)

        coEvery { movieApi.getMovieById(1) } returns movieRemote

        val result = movieRepository.getMovieDetailsApi(1)

        assertEquals(movieModel, result)

        coVerify { movieApi.getMovieById(1) }
    }

    @Test
    fun `getMovieDetailsApi returns null on failure`() = runTest {
        coEvery { movieApi.getMovieById(any()) } throws Exception("Network error")

        val result = movieRepository.getMovieDetailsApi(1)

        assertEquals(null, result)

        coVerify { movieApi.getMovieById(1) }
    }

    @Test
    fun `addMovieToFavorites inserts movie into dao`() = runTest {
        val movieModel = MovieModel(
            id = 1,
            title = "Favorite Movie",
            overview = "A movie to favorite",
            releaseDate = "2023-08-01",
            popularity = 8.5,
            posterPath = "poster_path",
            isFavorite = true,
            backdropPath = "backdrop_path",
            genreIds = listOf(1, 2, 3),
            originalLanguage = "en",
            originalTitle = "Original Title",
            voteAverage = 7.5,
            voteCount = 100,
            video = false,
            adult = false
        )
        val movieEntity = movieMapper.toEntity(movieModel)

        coEvery { movieDao.insertMovie(movieEntity) } just runs
        coEvery { movieDao.updateFavorite(movieModel.id, true) } returns 1

        movieRepository.addMovieToFavorites(movieModel)

        coVerify(exactly = 1) { movieDao.insertMovie(movieEntity) }
        coVerify(exactly = 1) { movieDao.updateFavorite(movieModel.id, true) }
    }
}
