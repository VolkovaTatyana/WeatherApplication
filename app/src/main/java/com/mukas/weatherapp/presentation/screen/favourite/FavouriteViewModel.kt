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
import com.mukas.weatherapp.presentation.util.toCityItemInitial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
                .collect { cities ->
                    val cityItems = cities.map { it.toCityItemInitial() }
                    changeWeatherState(cityItems)
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

    private suspend fun changeWeatherState(cities: List<FavouriteState.CityItem>) {
        _state.value = _state.value.copy(cityItems = cities)

        val loadingCitiesList = cities.map { cityItem ->
            cityItem.copy(
                weatherState = FavouriteState.WeatherState.Loading(
                    cityId = cityItem.city.id
                )
            )
        }
        _state.value = _state.value.copy(cityItems = loadingCitiesList)

        val resultList = viewModelScope.async(Dispatchers.IO) {
            loadingCitiesList.map { cityItem ->
                val weatherState = viewModelScope.async(Dispatchers.IO) {
                    loadWeatherForCity(cityItem.city)
                }.await()
                cityItem.copy(weatherState = weatherState)
            }
        }.await()
        _state.value = _state.value.copy(cityItems = resultList)
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