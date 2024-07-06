package com.andmar.weather.data

import kotlinx.coroutines.flow.Flow

class WeatherOfflineRepository(private val weatherDao: WeatherDao): WeatherRepository {

    override suspend fun insertLocationItem(locationItem: LocationItem) = weatherDao.insertLocationItem(locationItem)
    override suspend fun deleteLocationItem(locationItem: LocationItem) = weatherDao.deleteLocationItem(locationItem)
    override fun getLocationItems(): Flow<List<LocationItem>> = weatherDao.getLocationItems()
}
