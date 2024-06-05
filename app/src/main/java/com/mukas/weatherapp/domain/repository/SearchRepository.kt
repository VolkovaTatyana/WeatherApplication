package com.mukas.weatherapp.domain.repository

import com.mukas.weatherapp.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>
}