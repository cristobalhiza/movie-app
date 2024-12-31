package com.example.movie_app_hiza.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.movie_app_hiza.presentation.viewmodel.MovieViewModel

@Composable
fun MovieDetailsScreen(
    id: Int,
    viewModel: MovieViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val movie by viewModel.movieDetailsState.collectAsState()
    val favorites by viewModel.favoritesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFavoriteMovies()
    }
    LaunchedEffect(id) {
        viewModel.fetchMovieDetails(id)
    }

    movie?.let {
        val isFavorite = favorites.any { fav -> fav.id == movie?.id }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Backdrop Image
            if (!it.backdropPath.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${it.backdropPath}"),
                    contentDescription = "Backdrop Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Poster and Title Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Movie Poster
                if (!it.posterPath.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${it.posterPath}"),
                        contentDescription = "Poster Image",
                        modifier = Modifier
                            .width(120.dp)
                            .height(180.dp)
                            .padding(end = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                // Title, Rating, and Favorite Button
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Rating
                    Text(
                        text = "⭐ ${String.format("%.1f", it.voteAverage ?: "N/A")}",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Favorite Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (isFavorite) {
                                    viewModel.removeMovieFromFavorites(it.id)
                                    Toast.makeText(
                                        context,
                                        "Removed from favorites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.addMovieToFavorites(it)
                                    Toast.makeText(
                                        context,
                                        "Added to favorites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = if (isFavorite) "Added to Favorites" else "Add to Favorites",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Overview Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Release Date and Popularity
                Text(
                    text = "Release Date: ${it.formattedYear}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Popularity: ${it.popularity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

}

