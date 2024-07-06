package com.andmar.weather.ui.weather

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.andmar.weather.rest.WeatherService
import com.andmar.weather.data.WeatherDataStore
import com.andmar.weather.data.WeatherRepository

class DetailsViewModel(
    private val weatherRepository: WeatherRepository,
    private val weatherService: WeatherService,
    private val weatherDataStore: WeatherDataStore
): ViewModel() {

    var detailsUiState by mutableStateOf(DetailsUiState())
        private set
        
   init {
       viewModelScope.launch {
           detailsUiState = DetailsUiState(
           weatherService.weather.toWeatherUi(
               weatherDataStore.getSettings
                .filterNotNull()
                .first()
           )
       )
       }
   }     
        

}

data class DetailsUiState(
    val weather: WeatherUi = WeatherUi()
)

