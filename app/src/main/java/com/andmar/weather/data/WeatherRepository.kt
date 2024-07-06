package com.andmar.weather.data

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun insertLocationItem(locationItem: LocationItem)
    suspend fun deleteLocationItem(locationItem: LocationItem)
    fun getLocationItems(): Flow<List<LocationItem>>
}