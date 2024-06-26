package com.mukas.weatherapp.presentation.screen.details

import com.mukas.weatherapp.navigation.Screen
import com.mukas.weatherapp.navigation.ScreenDestination

class DetailsScreenDestination(
    cityId: Int,
    cityName: String,
    country: String
) : ScreenDestination() {

    override val route = Screen.Details(cityId, cityName, country)
}