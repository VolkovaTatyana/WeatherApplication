package com.mukas.weatherapp.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukas.weatherapp.domain.entity.City
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
    private val router: Router,
    private val searchCity: SearchCityUseCase
) : ViewModel() {

    private val _model = MutableStateFlow(State("", State.SearchState.Initial))
    val model = _model.asStateFlow()

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

    fun changeSearchQuery(query: String) {
        _model.value = model.value.copy(searchQuery = query)
    }

    fun onClickBack() {
        router.pop()
    }

    fun onClickSearch() {
        val query = _model.value.searchQuery
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cities = searchCity(query)
                withContext(Dispatchers.Main) {
                    if (cities.isEmpty()) {
                        _model.value =
                            _model.value.copy(searchState = State.SearchState.EmptyResult)
                    } else {
                        _model.value =
                            _model.value.copy(searchState = State.SearchState.SuccessLoaded(cities))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _model.value = _model.value.copy(searchState = State.SearchState.Error)
                }
            }
        }
    }

    fun onClickCity(city: City) {
        router.navigate(
            DetailsScreenDestination(
                cityId = city.id,
                cityName = city.name,
                country = city.country
            )
        )
    }
}