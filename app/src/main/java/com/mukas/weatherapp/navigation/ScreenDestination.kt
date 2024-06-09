package com.mukas.weatherapp.navigation

import androidx.navigation.NavOptionsBuilder

abstract class ScreenDestination {

    abstract val route: String
    open val builder: NavOptionsBuilder.() -> Unit = {}

}