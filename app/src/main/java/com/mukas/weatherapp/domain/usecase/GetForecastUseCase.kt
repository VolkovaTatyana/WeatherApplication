package com.mukas.weatherapp.domain.usecase

import com.mukas.weatherapp.domain.entity.Forecast
import com.mukas.weatherapp.domain.repository.WeatherRepository


class GetForecastUseCase(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(cityId: Int): Forecast = repository.getForecast(cityId)
}