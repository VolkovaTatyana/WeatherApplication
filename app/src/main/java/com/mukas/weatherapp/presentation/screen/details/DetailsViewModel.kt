package com.mukas.weatherapp.presentation.screen.details

import androidx.lifecycle.ViewModel
import com.mukas.weatherapp.domain.entity.City
import com.mukas.weatherapp.domain.entity.Forecast
import com.mukas.weatherapp.navigation.Router
import com.mukas.weatherapp.navigation.pop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailsViewModel(
    private val router: Router
) : ViewModel() {

    val model: StateFlow<State> = MutableStateFlow(
        State(
            city = City(0, "", ""),
            isFavourite = false,
            forecastState = State.ForecastState.Initial
        )
    )

    data class State(
        val city: City,
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