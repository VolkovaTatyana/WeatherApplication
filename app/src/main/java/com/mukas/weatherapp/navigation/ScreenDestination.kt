package com.mukas.weatherapp.navigation

import kotlinx.serialization.Serializable


abstract class ScreenDestination {

    abstract val route: Screen
}

@Serializable
sealed interface Screen {
    @Serializable
    data object Favourite : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data class Details(val cityId: Int) : Screen
}