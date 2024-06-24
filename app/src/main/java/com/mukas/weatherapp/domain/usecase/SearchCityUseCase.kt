package com.mukas.weatherapp.domain.usecase

import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.repository.SearchRepository


class SearchCityUseCase(
    private val repository: SearchRepository
) {

    suspend operator fun invoke(query: String): List<City> = repository.search(query)
}