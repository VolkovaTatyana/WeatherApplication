package com.mukas.weatherapp.presentation.screen.search

import com.mukas.weatherapp.domain.entity.City

sealed class SearchAction {

    class ClickCity(val city: City): SearchAction()
    class ChangeSearchQuery(val query: String): SearchAction()
    data object ClickSearch : SearchAction()
    data object ClickBack : SearchAction()
}