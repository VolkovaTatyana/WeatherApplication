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
    data class Search(val isSearchToAddFavourite: Boolean) : Screen

    @Serializable
    data class Details(
        val citiesAmount: Int,
        val cityPositionInList: Int,
        val cityId: Int,
        val cityName: String,
        val country: String
    ) : Screen
}