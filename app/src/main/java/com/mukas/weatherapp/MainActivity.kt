package com.mukas.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mukas.weatherapp.navigation.Router
import com.mukas.weatherapp.navigation.Screen
import com.mukas.weatherapp.presentation.screen.details.DetailsScreen
import com.mukas.weatherapp.presentation.screen.favourite.FavouriteScreen
import com.mukas.weatherapp.presentation.screen.search.SearchScreen
import com.mukas.weatherapp.presentation.theme.WeatherApplicationTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val router by inject<Router>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WeatherApplicationTheme {
                val navController = rememberNavController()
                router.attach(navController)
                /*NavHost(
                    navController = navController,
                    startDestination = FavouriteScreenDestination.ROUTE
                ) {
                    composable(route = FavouriteScreenDestination.ROUTE) {
                        FavouriteScreen()
                    }
                    composable(route = SearchScreenDestination.ROUTE) {
                        SearchScreen()
                    }
                    composable(route = DetailsScreenDestination.ROUTE) {
                        val cityName = it.arguments?.getString(DetailsScreenDestination.ARG_KEY) ?: "empty"
                        DetailsScreen(cityName)
                    }
                }*/

                NavHost(
                    navController = navController,
                    startDestination = Screen.Favourite
                ) {
                    composable<Screen.Favourite> {
                        FavouriteScreen()
                    }
                    composable<Screen.Search> {
                        SearchScreen()
                    }
                    composable<Screen.Details> {
                        val args = it.toRoute<Screen.Details>()
                        val cityId = args.cityId
                        DetailsScreen(cityId)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        router.detach()
        super.onDestroy()
    }
}
