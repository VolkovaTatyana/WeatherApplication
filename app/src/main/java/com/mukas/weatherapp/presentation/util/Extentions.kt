package com.mukas.weatherapp.presentation.util

import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.entity.Forecast
import com.mukas.weatherapp.domain.entity.Weather
import com.mukas.weatherapp.presentation.screen.details.ForecastUi
import com.mukas.weatherapp.presentation.screen.details.WeatherUi
import com.mukas.weatherapp.presentation.screen.favourite.FavouriteState
import kotlinx.collections.immutable.toPersistentList
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

fun Weather.toWeatherUi(): WeatherUi {
    return WeatherUi(
        tempC = this.tempC,
        conditionText = this.conditionText,
        conditionUrl = this.conditionUrl,
        formattedFullDate = this.date.formattedFullDate(),
        formattedShortDayOfWeek = this.date.formattedShortDayOfWeek()
    )
}

fun Forecast.toForecastUi(): ForecastUi {
    return ForecastUi(
        currentWeather = this.currentWeather.toWeatherUi(),
        upcoming = this.upcoming.map {
            it.toWeatherUi()
        }.toPersistentList()
    )
}