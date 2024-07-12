package com.mukas.weatherapp.presentation.screen.details

import kotlinx.collections.immutable.ImmutableList

data class ForecastUi(
    val currentWeather: WeatherUi,
    val upcoming: ImmutableList<WeatherUi>
)