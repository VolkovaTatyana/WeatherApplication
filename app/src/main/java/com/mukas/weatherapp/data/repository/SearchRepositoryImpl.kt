package com.mukas.weatherapp.data.repository

import com.mukas.weatherapp.data.mapper.toEntities
import com.mukas.weatherapp.data.network.api.ApiService
import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.repository.SearchRepository


class SearchRepositoryImpl(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun search(query: String): List<City> {
        return apiService.searchCity(query).toEntities()
    }
}