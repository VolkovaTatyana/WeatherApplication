package com.mukas.weatherapp.presentation.screen.search

import com.mukas.weatherapp.navigation.Screen
import com.mukas.weatherapp.navigation.ScreenDestination

class SearchScreenDestination(isSearchToAddFavourite: Boolean = false) : ScreenDestination() {

    override val route = Screen.Search(isSearchToAddFavourite)
}