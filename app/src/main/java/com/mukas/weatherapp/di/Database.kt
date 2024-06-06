package com.mukas.weatherapp.di

import androidx.room.Room
import com.mukas.weatherapp.data.local.db.FavouriteDatabase
import com.mukas.weatherapp.data.local.db.FavouriteDatabase.Companion.DB_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), FavouriteDatabase::class.java, DB_NAME).build()
    }

    single { get<FavouriteDatabase>().favouriteCitiesDao }
}