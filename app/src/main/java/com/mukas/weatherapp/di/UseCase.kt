package com.mukas.weatherapp.di

import com.mukas.weatherapp.domain.usecase.ChangeFavouriteStateUseCase
import com.mukas.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.mukas.weatherapp.domain.usecase.GetFavouriteStateUseCase
import com.mukas.weatherapp.domain.usecase.GetForecastUseCase
import com.mukas.weatherapp.domain.usecase.ObserveFavouriteCitiesUseCase
import com.mukas.weatherapp.domain.usecase.SearchCityUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { ChangeFavouriteStateUseCase(get()) }
    factory { GetCurrentWeatherUseCase(get()) }
    factory { ObserveFavouriteCitiesUseCase(get()) }
    factory { GetForecastUseCase(get()) }
    factory { GetFavouriteStateUseCase(get()) }
    factory { SearchCityUseCase(get()) }
}