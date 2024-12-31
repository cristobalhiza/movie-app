package com.example.movie_app_hiza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.movie_app_hiza.presentation.navigation.NavGraph
import com.example.movie_app_hiza.presentation.navigation.Screen
import com.example.movie_app_hiza.ui.theme.MovieapphizaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieApp() {
    MovieapphizaTheme {
        val navController = rememberNavController()

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Movie App",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            bottomBar = {
                BottomNavigationBar(navController)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            NavGraph(
                navController = navController,
                modifier = Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.PopularMovies.route) },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Popular") },
            label = { Text("Popular") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.Favorites.route) },
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.SearchMovies.route) },
            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            label = { Text("Search") }
        )
    }
}
