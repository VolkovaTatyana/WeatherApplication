package com.mukas.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
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

                NavHost(
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    navController = navController,
                    startDestination = Screen.Favourite
                ) {
                    composable<Screen.Favourite> {
                        FavouriteScreen()
                    }
                    composable<Screen.Search> {
                        val args = it.toRoute<Screen.Search>()
                        SearchScreen(args.isSearchToAddFavourite)
                    }
                    composable<Screen.Details> {
                        val args = it.toRoute<Screen.Details>()
                        DetailsScreen(
                            citiesAmount = args.citiesAmount,
                            cityPositionInList = args.cityPositionInList,
                            cityId = args.cityId,
                            cityName = args.cityName,
                            country = args.country,
                            addingTime = args.addingTime
                        )
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
