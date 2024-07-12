package com.mukas.weatherapp.presentation.screen.details

import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.entity.Forecast

data class DetailsState(
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