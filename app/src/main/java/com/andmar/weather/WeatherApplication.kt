package com.andmar.weather

import android.util.Log
import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.location.Location
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.LocationServices
import com.andmar.weather.rest.WeatherService
import com.andmar.weather.data.WeatherDataStore
import com.andmar.weather.data.WeatherContainer
import com.andmar.weather.data.WeatherAppContainer
import com.andmar.weather.location.LocationClient
import com.andmar.weather.location.DefaultLocationClient

private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
   private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
   )

class WeatherApplication: Application() {
    
    lateinit var locationClient: LocationClient
    lateinit var weatherService: WeatherService
    lateinit var weatherDataStore: WeatherDataStore
    lateinit var weatherAppContainer: WeatherContainer
    
    override fun onCreate() {
        super.onCreate()
        
        locationClient = DefaultLocationClient(
            this,
            LocationServices.getFusedLocationProviderClient(this)
        )
        weatherService = WeatherService()
        weatherDataStore = WeatherDataStore(dataStore)
        weatherAppContainer = WeatherAppContainer(this)
    }
}
