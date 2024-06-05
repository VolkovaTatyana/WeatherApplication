package com.mukas.weatherapp.data.mapper

import com.mukas.weatherapp.data.local.model.CityDbModel
import com.mukas.weatherapp.domain.entity.City

fun City.toDbModel(): CityDbModel = CityDbModel(id = id, name = name, country = country)

fun CityDbModel.toEntity(): City = City(id = id, name = name, country = country)

fun List<CityDbModel>.toEntities(): List<City> = map { it.toEntity() }