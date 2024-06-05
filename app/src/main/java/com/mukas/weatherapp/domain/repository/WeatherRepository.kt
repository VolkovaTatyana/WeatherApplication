package com.mukas.weatherapp.domain.repository

import com.mukas.weatherapp.domain.entity.Forecast
import com.mukas.weatherapp.domain.entity.Weather


interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast

}