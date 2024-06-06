package com.mukas.weatherapp.di

import com.mukas.weatherapp.domain.usecase.ChangeFavouriteStateUseCase
import com.mukas.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.mukas.weatherapp.domain.usecase.GetFavouriteCitiesUseCase
import com.mukas.weatherapp.domain.usecase.GetForecastUseCase
import com.mukas.weatherapp.domain.usecase.ObserveFavouriteStateUseCase
import com.mukas.weatherapp.domain.usecase.SearchCityUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { ChangeFavouriteStateUseCase(get()) }
    factory { GetCurrentWeatherUseCase(get()) }
    factory { GetFavouriteCitiesUseCase(get()) }
    factory { GetForecastUseCase(get()) }
    factory { ObserveFavouriteStateUseCase(get()) }
    factory { SearchCityUseCase(get()) }
}