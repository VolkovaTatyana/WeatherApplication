package com.mukas.weatherapp.presentation.theme.screen.search

import androidx.lifecycle.ViewModel
import com.mukas.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel : ViewModel() {

    val model: StateFlow<State> = MutableStateFlow(State("", State.SearchState.Initial))

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {

        sealed interface SearchState {
            data object Initial : SearchState

            data object Loading : SearchState

            data object Error : SearchState

            data object EmptyResult : SearchState

            data class SuccessLoaded(val cities: List<City>) : SearchState
        }
    }

    fun changeSearchQuery(query: String) {}

    fun onClickBack() {}

    fun onClickSearch() {}

    fun onClickCity(city: City) {}
}