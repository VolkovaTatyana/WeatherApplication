package com.mukas.weatherapp.navigation

sealed class Command {

    object Pop : Command()
    class Navigate(val destination: ScreenDestination) : Command()

}
