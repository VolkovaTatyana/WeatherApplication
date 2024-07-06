package com.mukas.weatherapp.presentation.util

import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.presentation.screen.favourite.FavouriteState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

fun Float.tempToFormattedString(): String = "${roundToInt()}°C"

fun Calendar.formattedFullDate(): String {
    val format = SimpleDateFormat("EEEE | d MMM y", Locale.getDefault())
    return format.format(time)
}

fun Calendar.formattedShortDayOfWeek(): String {
    val format = SimpleDateFormat("EEE", Locale.getDefault())
    return format.format(time)
}

fun City.toCityItemInitial(): FavouriteState.CityItem {
    return FavouriteState.CityItem(
        city = this,
        weatherState = FavouriteState.WeatherState.Initial
    )
}