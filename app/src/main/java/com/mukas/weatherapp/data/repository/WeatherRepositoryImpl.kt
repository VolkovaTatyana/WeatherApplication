package com.mukas.weatherapp.data.repository

import com.mukas.weatherapp.data.mapper.toEntity
import com.mukas.weatherapp.data.network.api.ApiService
import com.mukas.weatherapp.domain.entity.Forecast
import com.mukas.weatherapp.domain.entity.Weather
import com.mukas.weatherapp.domain.repository.WeatherRepository


class WeatherRepositoryImpl(
    private val apiService: ApiService
): WeatherRepository {
    override suspend fun getWeather(cityId: Int): Weather {
        return apiService.loadCurrentWeather("$PREFIX_CITY_ID$cityId").toEntity()
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        return apiService.loadForecast("$PREFIX_CITY_ID$cityId").toEntity()
    }

    private companion object {

        private const val PREFIX_CITY_ID = "id:"
    }

}