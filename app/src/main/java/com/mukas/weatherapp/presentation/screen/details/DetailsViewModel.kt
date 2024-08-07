package com.mukas.weatherapp.presentation.screen.details

import androidx.lifecycle.viewModelScope
import com.mukas.weatherapp.base.BaseViewModel
import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.usecase.ChangeFavouriteStateUseCase
import com.mukas.weatherapp.domain.usecase.GetForecastUseCase
import com.mukas.weatherapp.domain.usecase.ObserveFavouriteCitiesUseCase
import com.mukas.weatherapp.domain.usecase.ObserveFavouriteStateUseCase
import com.mukas.weatherapp.navigation.Router
import com.mukas.weatherapp.navigation.pop
import com.mukas.weatherapp.presentation.util.toForecastUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(
    val citiesAmount: Int,
    val cityPositionInList: Int,
    cityId: Int,
    cityName: String,
    country: String,
    private val observeFavouriteCities: ObserveFavouriteCitiesUseCase,
    private val getForecast: GetForecastUseCase,
    private val observeFavouriteStateUseCase: ObserveFavouriteStateUseCase,
    private val changeFavouriteState: ChangeFavouriteStateUseCase,
    private val router: Router
) : BaseViewModel<DetailsState>() {

    private val city = City(id = cityId, name = cityName, country = country)

    private lateinit var cityList: List<City>

    override fun createInitialState(): DetailsState {
        return DetailsState(
            citiesAmount = citiesAmount,
            cityPositionInList = cityPositionInList,
            city = city,
            isFavourite = false,
            forecastState = DetailsState.ForecastState.Initial
        )
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                cityList = observeFavouriteCities().first()
            }
        }

        changeForecastState(DetailsState.ForecastState.Loading)

        observeFavouriteState(cityId)
    }

    private fun observeFavouriteState(cityId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                observeFavouriteStateUseCase(cityId)
            }
                .collect {
                    act(DetailsAction.FavouriteStateChanged(it))
                }
        }
    }

    private fun changeForecastState(forecastState: DetailsState.ForecastState) {
        _state.update {
            it.copy(forecastState = forecastState)
        }

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

    private suspend fun loadForecast(): DetailsState.ForecastState = withContext(Dispatchers.IO) {
        try {
            val forecast = getForecast(cityId = _state.value.city.id).toForecastUi()
            DetailsState.ForecastState.Loaded(forecast)
        } catch (e: Exception) {
            DetailsState.ForecastState.Error
        }
    }

    fun act(action: DetailsAction) {
        when (action) {
            DetailsAction.ClickBack -> {
                router.pop()
            }

            DetailsAction.ClickChangeFavouriteState -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (_state.value.isFavourite) {
                        changeFavouriteState.removeFromFavourite(cityId = _state.value.city.id)
                    } else {
                        changeFavouriteState.addToFavourite(_state.value.city)
                    }
                }
            }

            is DetailsAction.FavouriteStateChanged -> {
                _state.update {
                    it.copy(isFavourite = action.isFavourite)
                }
            }

            is DetailsAction.PagerStateChanged -> {
                setNewState(action.page)
            }
        }
    }

    private fun setNewState(position: Int) {
        val city = cityList[position]
        _state.update {
            it.copy(
                city = city,
                cityPositionInList = position,
                isFavourite = true,
                forecastState = DetailsState.ForecastState.Initial
            )
        }
        observeFavouriteState(city.id)
        changeForecastState(DetailsState.ForecastState.Loading)
    }
}