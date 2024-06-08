package com.mukas.weatherapp.di

import com.mukas.weatherapp.presentation.screen.details.DetailsViewModel
import com.mukas.weatherapp.presentation.screen.favourite.FavouriteViewModel
import com.mukas.weatherapp.presentation.screen.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { FavouriteViewModel() }

    viewModel { SearchViewModel() }

    viewModel { DetailsViewModel() }
}