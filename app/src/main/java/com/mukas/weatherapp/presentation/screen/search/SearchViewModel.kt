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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                changeRequestState(newRequestState = SearchState.RequestState.Initial)
            }
        }
    }

    private fun changeRequestState(newRequestState: SearchState.RequestState) {
        when (newRequestState) {

            SearchState.RequestState.Initial -> {
                _state.value = _state.value.copy(requestState = newRequestState)
                changeRequestState(newRequestState = SearchState.RequestState.Loading)
            }

            SearchState.RequestState.Loading -> {
                _state.value = _state.value.copy(requestState = newRequestState)

                viewModelScope.launch(Dispatchers.IO) {
                    val query = _state.value.searchQuery
                    val resultingRequestState =
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
                    withContext(Dispatchers.Main) {
                        changeRequestState(resultingRequestState)
                    }
                }
            }

            SearchState.RequestState.EmptyResult -> {
                _state.value = _state.value.copy(requestState = newRequestState)
            }

            SearchState.RequestState.Error -> {
                _state.value = _state.value.copy(requestState = newRequestState)
            }

            is SearchState.RequestState.SuccessLoaded -> {
                _state.value = _state.value.copy(requestState = newRequestState)
            }
        }
    }

    fun onClickSearch() {
        val query = _state.value.searchQuery
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cities = searchCity(query)
                withContext(Dispatchers.Main) {
                    if (cities.isEmpty()) {
                        _state.value =
                            _state.value.copy(requestState = SearchState.RequestState.EmptyResult)
                    } else {
                        _state.value =
                            _state.value.copy(
                                requestState = SearchState.RequestState.SuccessLoaded(
                                    cities
                                )
                            )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(requestState = SearchState.RequestState.Error)
                }
            }
        }
    }
}