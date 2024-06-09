package com.mukas.weatherapp.presentation.screen.search

import com.mukas.weatherapp.navigation.ScreenDestination

class SearchScreenDestination : ScreenDestination() {

    override val route = ROUTE
    companion object {

        const val ROUTE = "/search"
    }
}