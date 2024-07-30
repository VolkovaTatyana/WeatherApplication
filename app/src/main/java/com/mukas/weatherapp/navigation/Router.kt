package com.mukas.weatherapp.navigation

import androidx.navigation.NavHostController

interface Router {

    fun attach(navController: NavHostController)
    fun execute(command: Command)
    fun detach()

    private class ComposeRouter : Router {

        private var navController: NavHostController? = null

        override fun attach(navController: NavHostController) {
            this.navController = navController
        }

        override fun execute(command: Command) {
            when(command) {
                Command.Pop -> performPop()
                is Command.Navigate -> performNavigate(destination = command.destination)
            }
        }

        private fun performPop() {
            navController?.navigateUp()
        }

        private fun performNavigate(destination: ScreenDestination) {
            navController?.navigate(route = destination.route)
        }

        override fun detach() {
            navController = null
        }

    }

    interface Factory {
        companion object {
            fun create(): Router {
                return ComposeRouter()
            }
        }
    }

}