package com.mukas.weatherapp.domain.usecase

import com.mukas.weatherapp.domain.repository.SearchRepository


class SearchCityUseCase(
    private val repository: SearchRepository
) {

    suspend operator fun invoke(query: String) = repository.search(query)
}