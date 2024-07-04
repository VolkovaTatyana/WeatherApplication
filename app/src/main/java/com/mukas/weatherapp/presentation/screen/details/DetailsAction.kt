package com.mukas.weatherapp.presentation.screen.details

sealed class DetailsAction {

    data object ClickBack : DetailsAction()

    data object ClickChangeFavouriteState : DetailsAction()

    data class FavouriteStateChanged(val isFavourite: Boolean) : DetailsAction()
}