package com.mukas.weatherapp.data.repository

import com.mukas.weatherapp.data.local.db.FavouriteCitiesDao
import com.mukas.weatherapp.data.mapper.toDbModel
import com.mukas.weatherapp.data.mapper.toEntities
import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FavouriteRepositoryImpl(
    private val favouriteCitiesDao: FavouriteCitiesDao
) : FavouriteRepository {
    override val favouriteCities: Flow<List<City>> = favouriteCitiesDao.getFavouriteCities()
        .map { it.toEntities() }

    override fun observeIsFavourite(cityId: Int): Flow<Boolean> =
        favouriteCitiesDao.observeIsFavourite(cityId)

    override suspend fun addToFavourite(city: City) =
        favouriteCitiesDao.addToFavourite(city.toDbModel())

    override suspend fun removeFromFavourite(cityId: Int) =
        favouriteCitiesDao.removeFromFavourite(cityId)
}