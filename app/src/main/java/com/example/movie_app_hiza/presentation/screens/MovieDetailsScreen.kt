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
            Text(
                text = it.title ?: "Unknown Title",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = if (isFavorite) "Liked" else "Like",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )

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
                                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = it.overview ?: "No overview available.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Año de estreno: ${it.formattedYear}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Puntuación: ${it.voteAverage ?: "N/A"} (${it.voteCount} votos)",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (!it.genreIds.isNullOrEmpty()) {
                    Text(
                        text = "Géneros: ${it.genreIds.joinToString(", ") { id -> getGenreName(id) }}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = "Popularidad: ${it.popularity}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack) {
                    Text("Volver")
                }
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

private fun getGenreName(genreId: Int): String {
    val genreMap = mapOf(
        28 to "Acción",
        12 to "Aventura",
        16 to "Animación",
        35 to "Comedia",
        80 to "Crimen",
        18 to "Drama",
        10751 to "Familiar",
        27 to "Terror",
        878 to "Ciencia Ficción"
    )
    return genreMap[genreId] ?: "Desconocido"
}
