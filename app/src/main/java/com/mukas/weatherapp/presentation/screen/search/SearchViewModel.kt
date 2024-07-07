package com.mukas.weatherapp.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukas.weatherapp.domain.usecase.ChangeFavouriteStateUseCase
import com.mukas.weatherapp.domain.usecase.SearchCityUseCase
import com.mukas.weatherapp.navigation.Router
import com.mukas.weatherapp.navigation.navigate
import com.mukas.weatherapp.navigation.pop
import com.mukas.weatherapp.presentation.screen.details.DetailsScreenDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    val isSearchToAddFavourite: Boolean,
    private val router: Router,
    private val searchCity: SearchCityUseCase,
    private val changeFavouriteState: ChangeFavouriteStateUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        SearchState(
            searchQuery = "",
            requestState = SearchState.RequestState.Initial
        )
    )
    val state = _state.asStateFlow()

    private fun changeRequestState(newRequestState: SearchState.RequestState) {
        _state.value = _state.value.copy(requestState = newRequestState)

        when (newRequestState) {

            SearchState.RequestState.Initial -> {
                changeRequestState(newRequestState = SearchState.RequestState.Loading)
            }

            SearchState.RequestState.Loading -> {
                viewModelScope.launch {
                    val resultingRequestState = getRequestState()
                    changeRequestState(resultingRequestState)
                }
            }

            else -> { //Do Nothing }
            }
        }
    }

    private suspend fun getRequestState(): SearchState.RequestState {
        return viewModelScope.async(Dispatchers.IO) {
            val query = _state.value.searchQuery
            try {
                val cities = searchCity(query)
                if (cities.isEmpty()) {
                    SearchState.RequestState.EmptyResult
                } else {
                    SearchState.RequestState.SuccessLoaded(cities)
                }
            } catch (e: Exception) {
                SearchState.RequestState.Error
            }
        }.await()
    }

    fun act(action: SearchAction) {
        when (action) {

            is SearchAction.ChangeSearchQuery -> {
                _state.value = state.value.copy(searchQuery = action.query)
            }

            SearchAction.ClickBack -> {
                router.pop()
            }

            is SearchAction.ClickCity -> {
                if (isSearchToAddFavourite) {
                    viewModelScope.launch {
                        changeFavouriteState.addToFavourite(action.city)
                    }
                    router.pop()
                } else {
                    router.navigate(
                        DetailsScreenDestination(
                            cityId = action.city.id,
                            cityName = action.city.name,
                            country = action.city.country
                        )
                    )
                }
            }

            SearchAction.ClickSearch -> {
                changeRequestState(newRequestState = SearchState.RequestState.Loading)
            }
        }
    }
}