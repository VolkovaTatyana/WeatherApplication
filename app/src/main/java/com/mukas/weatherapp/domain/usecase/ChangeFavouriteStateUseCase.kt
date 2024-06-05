package com.mukas.weatherapp.domain.usecase

import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.repository.FavouriteRepository

class ChangeFavouriteStateUseCase(
    private val repository: FavouriteRepository
) {

    suspend fun addToFavourite(city: City) = repository.addToFavourite(city)

    suspend fun removeFromFavourite(cityId: Int) = repository.removeFromFavourite(cityId)
}