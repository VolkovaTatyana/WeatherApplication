package com.mukas.weatherapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mukas.weatherapp.data.local.model.CityDbModel


@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase : RoomDatabase() {

    abstract val favouriteCitiesDao: FavouriteCitiesDao
    companion object {

        const val DB_NAME = "FavouriteDatabase"
    }
}