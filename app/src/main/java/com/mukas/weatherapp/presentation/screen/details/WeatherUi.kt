package com.mukas.weatherapp.presentation.screen.details

data class WeatherUi(
    val tempC: Float,
    val conditionText: String,
    val conditionUrl: String,
    val formattedFullDate: String,
    val formattedShortDayOfWeek: String
)