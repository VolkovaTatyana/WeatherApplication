package com.mukas.weatherapp.presentation.screen.details

import com.mukas.weatherapp.navigation.ScreenDestination

class DetailsScreenDestination(cityName: String) : ScreenDestination() {

//    override val route = "$ROUTE/${city.name}"
    override val route = "/details/${cityName}"
    companion object {

        const val ROUTE = "/details/{city_name}"
        const val ARG_KEY = "city_name"
    }
}