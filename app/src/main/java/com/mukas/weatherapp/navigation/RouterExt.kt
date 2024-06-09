package com.mukas.weatherapp.navigation

fun Router.pop() {
    execute(Command.Pop)
}

fun Router.navigate(destination: ScreenDestination) {
    execute(Command.Navigate(destination = destination))
}