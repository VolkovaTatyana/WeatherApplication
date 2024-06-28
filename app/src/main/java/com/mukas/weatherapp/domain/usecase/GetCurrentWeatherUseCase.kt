package com.mukas.weatherapp.domain.usecase

import com.mukas.weatherapp.domain.entity.Weather
import com.mukas.weatherapp.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(cityId: Int): Weather = repository.getWeather(cityId)
}