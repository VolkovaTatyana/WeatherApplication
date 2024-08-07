package com.mukas.weatherapp.presentation.screen.favourite

import androidx.lifecycle.viewModelScope
import com.mukas.weatherapp.base.BaseViewModel
import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.mukas.weatherapp.domain.usecase.ObserveFavouriteCitiesUseCase
import com.mukas.weatherapp.navigation.Router
import com.mukas.weatherapp.navigation.navigate
import com.mukas.weatherapp.presentation.screen.details.DetailsScreenDestination
import com.mukas.weatherapp.presentation.screen.search.SearchScreenDestination
import com.mukas.weatherapp.presentation.util.toCityItemInitial
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteViewModel(
    private val router: Router,
    private val observeFavouriteCities: ObserveFavouriteCitiesUseCase,
    private val getCurrentWeather: GetCurrentWeatherUseCase
) : BaseViewModel<FavouriteState>() {

    override fun createInitialState(): FavouriteState {
        return FavouriteState(persistentListOf())
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                observeFavouriteCities()
            }
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
        }.toImmutableList()
        _state.update {
            it.copy(cityItems = loadingCitiesList)
        }

        fetchWeatherState(loadingCitiesList)
    }

    private fun fetchWeatherState(cities: List<FavouriteState.CityItem>) {
        viewModelScope.launch {

            val resultList = cities.map { cityItem ->
                val deferred = loadWeatherForCity(cityItem.city)
                Pair(cityItem, deferred)
            }.map { pair ->
                val weatherState = pair.second.await()
                pair.first.copy(weatherState = weatherState)
            }.toImmutableList()

            _state.update {
                it.copy(cityItems = resultList)
            }
        }
    }

    private suspend fun loadWeatherForCity(city: City): Deferred<FavouriteState.WeatherState> =
        viewModelScope.async(Dispatchers.IO) {
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
                        citiesAmount = _state.value.cityItems.size,
                        cityPositionInList = _state.value.cityItems.indexOf(action.cityItem),
                        cityId = action.cityItem.city.id,
                        cityName = action.cityItem.city.name,
                        country = action.cityItem.city.country
                    )
                )
            }

            FavouriteAction.ClickAddFavourite -> {
                router.navigate(SearchScreenDestination(true))
            }

            FavouriteAction.ClickSearch -> {
                router.navigate(SearchScreenDestination())
            }

            FavouriteAction.Refresh -> {
                initLoadingState(_state.value.cityItems)
            }
        }
    }
}