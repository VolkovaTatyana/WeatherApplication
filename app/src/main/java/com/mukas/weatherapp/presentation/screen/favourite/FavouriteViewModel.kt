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
                    getStateForCities(it)
                }
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

    private suspend fun getStateForCities(cityList: List<City>) {
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