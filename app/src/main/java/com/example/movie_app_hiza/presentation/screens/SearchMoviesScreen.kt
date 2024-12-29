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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.movie_app_hiza.domain.models.MovieModel
import com.example.movie_app_hiza.presentation.viewmodel.MovieViewModel

@Composable
fun SearchMoviesScreen(
    viewModel: MovieViewModel = viewModel(),
    onMovieClick: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val moviesState by viewModel.popularMoviesState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Search Movies",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)
        )
        BasicTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.searchMovies(query, 1)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            if (moviesState.movies.isNotEmpty()) {
                items(moviesState.movies) { movie ->
                    MovieItem(movie = movie, onClick = onMovieClick)
                }
            } else {
                item {
                    Text(
                        text = "No results found",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
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
                text = movie.title ?: "Unknown Title",
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

