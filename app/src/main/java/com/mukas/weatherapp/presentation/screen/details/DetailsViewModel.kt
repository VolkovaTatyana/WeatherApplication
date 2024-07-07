package com.mukas.weatherapp.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.usecase.ChangeFavouriteStateUseCase
import com.mukas.weatherapp.domain.usecase.GetForecastUseCase
import com.mukas.weatherapp.domain.usecase.ObserveFavouriteStateUseCase
import com.mukas.weatherapp.navigation.Router
import com.mukas.weatherapp.navigation.pop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    cityId: Int,
    cityName: String,
    country: String,
    private val getForecast: GetForecastUseCase,
    private val observeFavouriteState: ObserveFavouriteStateUseCase,
    private val changeFavouriteState: ChangeFavouriteStateUseCase,
    private val router: Router
) : ViewModel() {

    private val _state = MutableStateFlow(
        DetailsState(
            city = City(id = cityId, name = cityName, country = country),
            isFavourite = false,
            forecastState = DetailsState.ForecastState.Initial
        )
    )
    val state = _state.asStateFlow()

    init {
        changeForecastState(DetailsState.ForecastState.Loading)

        viewModelScope.launch {
            observeFavouriteState(cityId)
                .collect {
                    act(DetailsAction.FavouriteStateChanged(it))
                }
        }
    }

    private fun changeForecastState(forecastState: DetailsState.ForecastState) {
        _state.value = _state.value.copy(forecastState = forecastState)

        when (forecastState) {

            DetailsState.ForecastState.Initial -> {
                changeForecastState(DetailsState.ForecastState.Loading)
            }

            DetailsState.ForecastState.Loading -> {
                viewModelScope.launch {
                    val resultingForecastState = loadForecast()
                    changeForecastState(resultingForecastState)
                }
            }

            else -> {
                //Do Nothing
            }
        }
    }

    private suspend fun loadForecast(): DetailsState.ForecastState {
        return viewModelScope.async(Dispatchers.IO) {
            try {
                val forecast = getForecast(cityId = _state.value.city.id)
                DetailsState.ForecastState.Loaded(forecast)
            } catch (e: Exception) {
                DetailsState.ForecastState.Error
            }
        }.await()
    }

    fun act(action: DetailsAction) {
        when (action) {
            DetailsAction.ClickBack -> {
                router.pop()
            }

            DetailsAction.ClickChangeFavouriteState -> {
                viewModelScope.launch {
                    if (_state.value.isFavourite) {
                        changeFavouriteState.removeFromFavourite(cityId = _state.value.city.id)
                    } else {
                        changeFavouriteState.addToFavourite(_state.value.city)
                    }
                }
            }

            is DetailsAction.FavouriteStateChanged -> {
                _state.value = _state.value.copy(isFavourite = action.isFavourite)
            }
        }
    }
}