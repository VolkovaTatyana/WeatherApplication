package com.mukas.weatherapp.presentation.screen.details

import com.mukas.weatherapp.navigation.Screen
import com.mukas.weatherapp.navigation.ScreenDestination

class DetailsScreenDestination(
    citiesAmount: Int = 1,
    cityPositionInList: Int = 0,
    cityId: Int,
    cityName: String,
    country: String
) : ScreenDestination() {

    override val route = Screen.Details(citiesAmount, cityPositionInList, cityId, cityName, country)
}