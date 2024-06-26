package com.mukas.weatherapp.presentation.screen.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.mukas.weatherapp.domain.usecase.GetFavouriteCitiesUseCase
import com.mukas.weatherapp.navigation.Router
import com.mukas.weatherapp.navigation.navigate
import com.mukas.weatherapp.presentation.screen.details.DetailsScreenDestination
import com.mukas.weatherapp.presentation.screen.search.SearchScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val router: Router,
    private val getFavouriteCities: GetFavouriteCitiesUseCase,
    private val getCurrentWeather: GetCurrentWeatherUseCase
) : ViewModel() {

    private val _model = MutableStateFlow(State(listOf()))
    val model = _model.asStateFlow()

    init {
        viewModelScope.launch {
            getFavouriteCities()
                .collect {
                    val cities = it.map { city: City -> //TODO
                        State.CityItem(
                            city = city,
                            weatherState = State.WeatherState.Loading
                        )
                    }
                    _model.value = _model.value.copy(cityItems = cities)
                }
        }
    }

    data class State(
        val cityItems: List<CityItem>
    ) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {
            data object Initial : WeatherState

            data object Loading : WeatherState

            data object Error : WeatherState

            data class Loaded(
                val tempC: Float,
                val iconUrl: String
            ) : WeatherState
        }
    }


    fun onClickSearch() {
        router.navigate(SearchScreenDestination())
    }

    fun onClickAddFavourite() {
        router.navigate(SearchScreenDestination(true))
    }

    fun onCityItemClick(city: City) {
        router.navigate(
            DetailsScreenDestination(
                cityId = city.id,
                cityName = city.name,
                country = city.country
            )
        )
    }

    private suspend fun loadWeatherForCity(city: City) {

    }
}