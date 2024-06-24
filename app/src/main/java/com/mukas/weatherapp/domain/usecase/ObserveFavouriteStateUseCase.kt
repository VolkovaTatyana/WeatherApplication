package com.mukas.weatherapp.domain.usecase

import com.mukas.weatherapp.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow


class ObserveFavouriteStateUseCase(
    private val repository: FavouriteRepository
) {

    operator fun invoke(cityId: Int): Flow<Boolean> = repository.observeIsFavourite(cityId)
}