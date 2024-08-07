package com.mukas.weatherapp.di

import com.mukas.weatherapp.navigation.Router
import com.mukas.weatherapp.presentation.screen.details.DetailsViewModel
import com.mukas.weatherapp.presentation.screen.favourite.FavouriteViewModel
import com.mukas.weatherapp.presentation.screen.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelModule = module {

    singleOf(Router.Factory::create)

    viewModel {
        FavouriteViewModel(
            router = get(),
            observeFavouriteCities = get(),
            getCurrentWeather = get()
        )
    }

    viewModel {
        SearchViewModel(
            isSearchToAddFavourite = get(),
            router = get(),
            searchCity = get(),
            changeFavouriteState = get()
        )
    }

    viewModel { parameters ->
        DetailsViewModel(
            citiesAmount = parameters.get(),
            cityPositionInList = parameters.get(),
            cityId = parameters.get(),
            cityName = parameters.get(),
            country = parameters.get(),
            observeFavouriteCities = get(),
            getForecast = get(),
            observeFavouriteStateUseCase = get(),
            changeFavouriteState = get(),
            router = get()
        )
    }
}