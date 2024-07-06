package com.andmar.weather.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras  
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.andmar.weather.WeatherApplication
import com.andmar.weather.ui.weather.WeatherViewModel
import com.andmar.weather.ui.location.LocationViewModel
import com.andmar.weather.ui.location.AddLocationViewModel
import com.andmar.weather.ui.weather.DetailsViewModel
import com.andmar.weather.ui.settings.SettingsViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {
    
        initializer {
            WeatherViewModel(
                weatherApplication().locationClient,
                weatherApplication().weatherService,
                weatherApplication().weatherDataStore,
                weatherApplication().weatherAppContainer.weatherRepository
            )
        }
        
        initializer {
            LocationViewModel(
                weatherApplication().weatherAppContainer.weatherRepository,
                weatherApplication().weatherDataStore
            )
        }
        
        initializer {
            AddLocationViewModel(
                weatherApplication().locationClient,
                weatherApplication().weatherService,
                weatherApplication().weatherDataStore,
                weatherApplication().weatherAppContainer.weatherRepository
            )
        }
        
        initializer {
            DetailsViewModel(
                weatherApplication().weatherAppContainer.weatherRepository,
                weatherApplication().weatherService,
                weatherApplication().weatherDataStore
            )
        }
        
        initializer {
            SettingsViewModel(
                weatherApplication().weatherDataStore,
            )
        }
    }
}

fun CreationExtras.weatherApplication(): WeatherApplication = 
    (this[AndroidViewModelFactory.APPLICATION_KEY] as WeatherApplication)
