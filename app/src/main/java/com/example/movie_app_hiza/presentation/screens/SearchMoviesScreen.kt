package com.example.movie_app_hiza.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.movie_app_hiza.domain.models.MovieModel
import com.example.movie_app_hiza.presentation.viewmodel.MovieViewModel

@Composable
fun SearchMoviesScreen(viewModel: MovieViewModel, onMovieClick: (Int) -> Unit) {
    var query by remember { mutableStateOf("") }
    val moviesState by viewModel.popularMoviesState.collectAsState()

    LaunchedEffect(query) {
        if (query.isNotBlank()) {
            kotlinx.coroutines.delay(300)
            viewModel.searchMovies(query, 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Search Movies",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BasicTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textStyle = TextStyle(
                color = Color.White
            ),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search for a movie...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            moviesState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            moviesState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = moviesState.error ?: "Something went wrong",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            moviesState.movies.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No results found",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(moviesState.movies) { movie ->
                        MovieItem(movie = movie, onClick = onMovieClick)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: MovieModel, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(movie.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movie.posterPath}"),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = movie.formattedYear,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}


