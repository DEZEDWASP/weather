package com.andmar.weather.data

import android.content.Context

interface WeatherContainer {
    val weatherRepository: WeatherRepository
}


class WeatherAppContainer(context: Context): WeatherContainer {
    override val weatherRepository: WeatherRepository by lazy {
        WeatherOfflineRepository(WeatherDatabase.getDatabase(context).weatherDao())
    }
}
