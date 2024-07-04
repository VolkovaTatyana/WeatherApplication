package com.mukas.weatherapp.presentation.screen.search

import com.mukas.weatherapp.domain.entity.City

data class SearchState(
    val searchQuery: String,
    val requestState: RequestState
) {

    sealed interface RequestState {
        data object Initial : RequestState

        data object Loading : RequestState

        data object Error : RequestState

        data object EmptyResult : RequestState

        data class SuccessLoaded(val cities: List<City>) : RequestState
    }
}
