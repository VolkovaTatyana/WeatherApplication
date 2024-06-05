package com.mukas.weatherapp.domain.usecase

import com.mukas.weatherapp.domain.repository.FavouriteRepository


class ObserveFavouriteStateUseCase(
    private val repository: FavouriteRepository
) {

    operator fun invoke(cityId: Int) = repository.observeIsFavourite(cityId)
}