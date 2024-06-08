package com.mukas.weatherapp.presentation.screen.favourite

import androidx.lifecycle.ViewModel
import com.mukas.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavouriteViewModel : ViewModel() {

    val model: StateFlow<State> = MutableStateFlow(State(listOf()))

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


    fun onClickSearch() {}

    fun onClickAddFavourite() {}

    fun onCityItemClick(city: City) {}
}