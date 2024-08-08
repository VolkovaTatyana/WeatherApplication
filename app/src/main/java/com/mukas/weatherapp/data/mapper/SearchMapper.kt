package com.mukas.weatherapp.data.mapper

import com.mukas.weatherapp.data.network.dto.CityDto
import com.mukas.weatherapp.domain.entity.City

fun CityDto.toEntity(): City =
    City(id = id, name = name, country = country, addingTime = System.currentTimeMillis())

fun List<CityDto>.toEntities(): List<City> = map { it.toEntity() }