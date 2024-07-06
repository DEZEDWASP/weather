package com.andmar.weather.ui.location

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.MutableState
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
import com.andmar.weather.data.LocationItem

class LocationViewModel(
    private val weatherRepository: WeatherRepository,
    private val weatherDataStore: WeatherDataStore,
): ViewModel() {

    var locationUiState by mutableStateOf(LocationUiState())
        private set
    
    init {
        viewModelScope.launch {
            locationUiState = LocationUiState(
                location = weatherDataStore.getLocation.first(),
                isEnabled = enabled(weatherDataStore.getLocation.first())
            )
        }
    } 
    
    fun updateLocationUiState(locationState: LocationUiState) = viewModelScope.launch {
        weatherDataStore.updateLocation(locationState.location)
        locationUiState = locationState
    }

    val locationItemListState: StateFlow<LocationItemListState> =
        weatherRepository.getLocationItems().map { LocationItemListState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = LocationItemListState()
            )
            
    
    
    
    fun deleteLocation(locationItem: LocationItem) = viewModelScope.launch {
        weatherRepository.deleteLocationItem(locationItem)
    }
    
    fun enabled(location: String?): Boolean {
        return if(location as String == "") false else true
    }
}

data class LocationItemListState(val locationItemList: List<LocationItem> = emptyList())

data class LocationUiState(
    val location: String = "",
    val isEnabled: Boolean = false
)