package com.mukas.weatherapp.domain.usecase

import com.mukas.weatherapp.domain.repository.FavouriteRepository


class GetFavouriteStateUseCase(
    private val repository: FavouriteRepository
) {

    suspend operator fun invoke(cityId: Int): Boolean = repository.isFavourite(cityId)
}