package com.mukas.weatherapp.presentation.screen.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukas.weatherapp.domain.entity.Forecast
import com.mukas.weatherapp.domain.usecase.GetForecastUseCase
import com.mukas.weatherapp.domain.usecase.ObserveFavouriteStateUseCase
import com.mukas.weatherapp.navigation.Router
import com.mukas.weatherapp.navigation.pop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(
    cityId: Int,
    cityName: String,
    private val getForecast: GetForecastUseCase,
    private val observeFavouriteState: ObserveFavouriteStateUseCase,
    private val router: Router
) : ViewModel() {

    private val _model = MutableStateFlow(
        State(
            cityName = cityName,
            isFavourite = false,
            forecastState = State.ForecastState.Initial
        )
    )
    val model = _model.asStateFlow()

    init {
        Log.d("DetailsViewModel", "$cityName id=$cityId")

        viewModelScope.launch {
            _model.value = _model.value.copy(forecastState = State.ForecastState.Loading)
            withContext(Dispatchers.IO) {
                try {
                    val forecast = getForecast(cityId)
                    withContext(Dispatchers.Main) {
                        _model.value =
                            _model.value.copy(forecastState = State.ForecastState.Loaded(forecast))
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _model.value = _model.value.copy(forecastState = State.ForecastState.Error)
                    }

                }
            }
        }

        viewModelScope.launch {
            observeFavouriteState(cityId)
                .collect {
                    _model.value = _model.value.copy(isFavourite = it)
                }
        }
    }

    data class State(
        val cityName: String,
        val isFavourite: Boolean,
        val forecastState: ForecastState
    ) {
        sealed interface ForecastState {
            data object Initial : ForecastState
            data object Loading : ForecastState

            data object Error : ForecastState

            data class Loaded(val forecast: Forecast) : ForecastState
        }
    }

    fun onClickBack() {
        router.pop()
    }

    fun onClickChangeFavouriteStatus() {}
}