package com.mukas.weatherapp.presentation.screen.favourite

sealed class FavouriteAction {

    data object Refresh : FavouriteAction()

    data object ClickSearch : FavouriteAction()

    data object ClickAddFavourite : FavouriteAction()

    class CityItemClick(val cityItem: FavouriteState.CityItem) : FavouriteAction()
}