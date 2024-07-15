package com.mukas.weatherapp.presentation.screen.search

import com.mukas.weatherapp.domain.entity.City
import kotlinx.collections.immutable.ImmutableList

data class SearchState(
    val searchQuery: String,
    val requestState: RequestState
) {

    sealed class RequestState {
        data object Initial : RequestState()

        data object Loading : RequestState()

        data object Error : RequestState()

        data object EmptyResult : RequestState()

        data class SuccessLoaded(val cities: ImmutableList<City>) : RequestState()
    }
}
