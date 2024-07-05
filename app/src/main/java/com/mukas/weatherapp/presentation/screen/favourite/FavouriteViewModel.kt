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

    private val _state = MutableStateFlow(FavouriteState(listOf()))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getFavouriteCities()
                .collect {
                    changeStateForCities(it)
                }
        }
    }

    fun act(action: FavouriteAction) {
        when (action) {
            is FavouriteAction.CityItemClick -> {
                router.navigate(
                    DetailsScreenDestination(
                        cityId = action.city.id,
                        cityName = action.city.name,
                        country = action.city.country
                    )
                )
            }

            FavouriteAction.ClickAddFavourite -> {
                router.navigate(SearchScreenDestination(true))
            }

            FavouriteAction.ClickSearch -> {
                router.navigate(SearchScreenDestination())
            }
        }
    }

    private suspend fun changeStateForCities(cityList: List<City>) {
        val cities = cityList.map { city: City -> //TODO
            FavouriteState.CityItem(
                city = city,
                weatherState = FavouriteState.WeatherState.Loading(cityId = city.id)
            )
        }
        _state.value = _state.value.copy(cityItems = cities)
        cities.map { cityItem ->
            val weatherState = loadWeatherForCity(cityItem.city)
            cityItem.copy(weatherState = weatherState)
        }.apply {
            _state.value = _state.value.copy(cityItems = this)
        }
    }

    private fun getWeatherState(weatherState: FavouriteState.WeatherState) {
        when (weatherState) {

            FavouriteState.WeatherState.Initial -> {

            }

            is FavouriteState.WeatherState.Loading -> {

            }

            is FavouriteState.WeatherState.Loaded -> {

            }

            is FavouriteState.WeatherState.Error -> {

            }
        }
    }

    private suspend fun loadWeatherForCity(city: City): FavouriteState.WeatherState {
        return try {
            val weather = getCurrentWeather(city.id)
            FavouriteState.WeatherState.Loaded(
                tempC = weather.tempC,
                iconUrl = weather.conditionUrl,
                cityId = city.id
            )
        } catch (e: Exception) {
            FavouriteState.WeatherState.Error(city.id)
        }
    }
}