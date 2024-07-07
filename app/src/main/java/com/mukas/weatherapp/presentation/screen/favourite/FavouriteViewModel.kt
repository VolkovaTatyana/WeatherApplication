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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    initLoadingState(cityItems)
                }
        }
    }

    private fun initLoadingState(cities: List<FavouriteState.CityItem>) {
        val loadingCitiesList = cities.map { cityItem ->
            cityItem.copy(
                weatherState = FavouriteState.WeatherState.Loading(
                    cityId = cityItem.city.id
                )
            )
        }
        _state.update {
            it.copy(cityItems = loadingCitiesList)
        }

        fetchWeatherState(loadingCitiesList)
    }

    private fun fetchWeatherState(cities: List<FavouriteState.CityItem>) {
        viewModelScope.launch {

            val resultList = cities.map { cityItem ->
                val weatherState = loadWeatherForCity(cityItem.city)
                cityItem.copy(weatherState = weatherState)
            }

            _state.update {
                it.copy(cityItems = resultList)
            }
        }
    }

    private suspend fun loadWeatherForCity(city: City): FavouriteState.WeatherState =
        withContext(Dispatchers.IO) {
            try {
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
}