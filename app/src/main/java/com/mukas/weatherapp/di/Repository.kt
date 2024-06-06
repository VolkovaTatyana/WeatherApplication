package com.mukas.weatherapp.di

import com.mukas.weatherapp.data.repository.FavouriteRepositoryImpl
import com.mukas.weatherapp.data.repository.SearchRepositoryImpl
import com.mukas.weatherapp.data.repository.WeatherRepositoryImpl
import com.mukas.weatherapp.domain.repository.FavouriteRepository
import com.mukas.weatherapp.domain.repository.SearchRepository
import com.mukas.weatherapp.domain.repository.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<FavouriteRepository> { FavouriteRepositoryImpl(get()) }
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
}