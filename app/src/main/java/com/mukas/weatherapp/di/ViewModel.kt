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

    viewModel { FavouriteViewModel(get()) }

    viewModel { SearchViewModel(get(), get()) }

    viewModel { DetailsViewModel(get(), get(), get(), get(), get(), get()) }
}