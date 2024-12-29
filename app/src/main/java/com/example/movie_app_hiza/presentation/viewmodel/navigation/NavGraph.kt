package com.example.movie_app_hiza.presentation.viewmodel.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movie_app_hiza.presentation.screens.*
import com.example.movie_app_hiza.presentation.viewmodel.MovieViewModel

sealed class Screen(val route: String) {
    data object PopularMovies : Screen("popular_movies")
    data object MovieDetails : Screen("movie_details/{movieId}") {
        fun createRoute(movieId: Int) = "movie_details/$movieId"
    }

    data object SearchMovies : Screen("search_movies")
    data object Favorites : Screen("favorites")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.PopularMovies.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(Screen.PopularMovies.route) {
            val movieViewModel = hiltViewModel<MovieViewModel>()
            PopularMoviesScreen(viewModel = movieViewModel) { movieId ->
                navController.navigate(Screen.MovieDetails.createRoute(movieId))
            }
        }
        composable(Screen.Favorites.route) {
            val movieViewModel = hiltViewModel<MovieViewModel>()
            FavoritesScreen(viewModel = movieViewModel) { movieId ->
                navController.navigate(Screen.MovieDetails.createRoute(movieId))
            }
        }

        composable(Screen.SearchMovies.route) {
            val movieViewModel = hiltViewModel<MovieViewModel>()
            SearchMoviesScreen(viewModel = movieViewModel) { movieId ->
                navController.navigate(Screen.MovieDetails.createRoute(movieId))
            }
        }
        composable(
            route = Screen.MovieDetails.route, arguments = listOf(navArgument("movieId") {
                type = androidx.navigation.NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("movieId") ?: 0
            val movieViewModel = hiltViewModel<MovieViewModel>()
            LaunchedEffect(id) {
                movieViewModel.fetchMovieDetails(id)
                movieViewModel.fetchFavoriteMovies()
            }
            MovieDetailsScreen(id = id, viewModel = movieViewModel) {
                navController.popBackStack()
            }
        }
    }
}
