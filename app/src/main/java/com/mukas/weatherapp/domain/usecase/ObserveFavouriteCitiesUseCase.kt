package com.mukas.weatherapp.domain.usecase

import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow


class ObserveFavouriteCitiesUseCase(
    private val repository: FavouriteRepository
) {

    operator fun invoke(): Flow<List<City>> = repository.favouriteCities
}