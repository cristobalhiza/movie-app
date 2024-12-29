package com.example.movie_app_hiza.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.movie_app_hiza.presentation.viewmodel.MovieViewModel
import com.example.movie_app_hiza.domain.models.MovieModel

@Composable
fun PopularMoviesScreen(viewModel: MovieViewModel, onMovieClick: (Int) -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.fetchPopularMovies(6)
    }

    val uiState = viewModel.popularMoviesState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text(
            text = "Popular Movies",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            uiState.value.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cargando películas...")
                }
            }
            uiState.value.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${uiState.value.error}")
                }
            }
            else -> {
                val moviesByGenre = groupMoviesByGenre(uiState.value.movies)

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    moviesByGenre.forEach { (genre, movies) ->
                        item {
                            GenreSection(
                                genre = genre,
                                movies = movies,
                                onMovieClick = onMovieClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GenreSection(
    genre: String,
    movies: List<MovieModel>,
    onMovieClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = genre,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(movies) { movie ->
                MovieItem(
                    posterPath = movie.posterPath,
                    title = movie.title,
                    onClick = { onMovieClick(movie.id) }
                )
            }
        }
    }
}

@Composable
fun MovieItem(
    posterPath: String?,
    title: String,
    onClick: () -> Unit
) {
    val baseUrl = "https://image.tmdb.org/t/p/w500"
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = if (posterPath != null) "$baseUrl$posterPath" else null,
            contentDescription = title,
            modifier = Modifier
                .width(100.dp)
                .height(150.dp)
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
    }
}

fun groupMoviesByGenre(movies: List<MovieModel>): Map<String, List<MovieModel>> {
    val genreMap = mapOf(
        28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        18 to "Drama",
        10751 to "Family",
        27 to "Horror",
        878 to "Science Fiction"
    )

    return movies.flatMap { movie ->
        movie.genreIds.mapNotNull { genreId ->
            genreMap[genreId]?.let { genreName -> genreName to movie }
        }
    }.groupBy({ it.first }, { it.second })
}
