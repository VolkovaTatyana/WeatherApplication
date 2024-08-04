package com.mukas.weatherapp.presentation.screen.favourite

import com.mukas.weatherapp.domain.entity.City

sealed class FavouriteAction {

    data object Refresh : FavouriteAction()

    data object ClickSearch : FavouriteAction()

    data object ClickAddFavourite : FavouriteAction()

    class CityItemClick(val city: City) : FavouriteAction()
}