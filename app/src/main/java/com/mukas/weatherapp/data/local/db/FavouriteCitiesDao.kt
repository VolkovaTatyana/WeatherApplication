package com.mukas.weatherapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mukas.weatherapp.data.local.model.CityDbModel
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouriteCitiesDao {

    @Query("SELECT * FROM favourite_cities")
    fun getFavouriteCities(): Flow<List<CityDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourite(cityDbModel: CityDbModel)

    @Query("DELETE FROM favourite_cities WHERE id=:cityId")
    suspend fun removeFromFavourite(cityId: Int)

    @Query("SELECT EXISTS (SELECT * FROM favourite_cities WHERE id=:cityId LIMIT 1)")
    suspend fun isFavourite(cityId: Int): Boolean
}