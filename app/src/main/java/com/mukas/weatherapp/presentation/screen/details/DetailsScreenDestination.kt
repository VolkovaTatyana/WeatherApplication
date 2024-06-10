package com.mukas.weatherapp.presentation.screen.details

import com.mukas.weatherapp.navigation.Screen
import com.mukas.weatherapp.navigation.ScreenDestination

class DetailsScreenDestination(val cityId: Int) : ScreenDestination() {

    override val route = Screen.Details(cityId)
}