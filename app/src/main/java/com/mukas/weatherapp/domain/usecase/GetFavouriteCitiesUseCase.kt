package com.mukas.weatherapp.domain.usecase

import com.mukas.weatherapp.domain.repository.FavouriteRepository


class GetFavouriteCitiesUseCase(
    private val repository: FavouriteRepository
) {

    operator fun invoke() = repository.favouriteCities
}