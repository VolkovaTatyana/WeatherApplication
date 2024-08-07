package com.mukas.weatherapp.presentation.screen.details

import com.mukas.weatherapp.domain.entity.City

data class DetailsState(
    val citiesAmount: Int,
    val cityPositionInList: Int,
    val city: City,
    val isFavourite: Boolean,
    val forecastState: ForecastState
) {
    sealed class ForecastState {
        data object Initial : ForecastState()
        data object Loading : ForecastState()

        data object Error : ForecastState()

        data class Loaded(val forecast: ForecastUi) : ForecastState()
    }
}