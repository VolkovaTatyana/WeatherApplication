package com.mukas.weatherapp.navigation

sealed class Command {

    data object Pop : Command()
    class Navigate(val destination: ScreenDestination) : Command()

}
