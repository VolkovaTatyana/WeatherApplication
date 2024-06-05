package com.mukas.weatherapp.domain.repository

import com.mukas.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.Flow


interface FavouriteRepository {

    val favouriteCities: Flow<List<City>>

    fun observeIsFavourite(cityId: Int): Flow<Boolean>

    suspend fun addToFavourite(city: City)

    suspend fun removeFromFavourite(cityId: Int)
}