package com.mukas.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mukas.weatherapp.presentation.screen.details.DetailsScreen
import com.mukas.weatherapp.presentation.screen.favourite.FavouriteScreen
import com.mukas.weatherapp.presentation.screen.search.SearchScreen
import com.mukas.weatherapp.presentation.theme.WeatherApplicationTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WeatherApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Favourite) {
                    composable<Favourite> {
                        FavouriteScreen()
                    }
                    composable<Search> {
                        SearchScreen()
                    }
                    composable<Detail> {
                        DetailsScreen()
                    }
                }
            }
        }
    }
}

@Serializable
object Favourite

@Serializable
object Search
@Serializable
object Detail
