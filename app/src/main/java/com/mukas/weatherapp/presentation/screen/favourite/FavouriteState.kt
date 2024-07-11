package com.mukas.weatherapp.presentation.screen.favourite

import com.mukas.weatherapp.domain.entity.City
import kotlinx.collections.immutable.ImmutableList

data class FavouriteState(
    val cityItems: ImmutableList<CityItem>
) {

    data class CityItem(
        val city: City,
        val weatherState: WeatherState
    )

    sealed class WeatherState {
        data object Initial : WeatherState()

        data class Loading(val cityId: Int) : WeatherState()

        data class Error(val cityId: Int) : WeatherState()

        data class Loaded(
            val cityId: Int,
            val tempC: Float,
            val iconUrl: String
        ) : WeatherState()
    }
}