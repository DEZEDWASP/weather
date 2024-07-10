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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.andmar.weather.rest.WeatherService
import com.andmar.weather.rest.Location
import com.andmar.weather.data.WeatherDataStore
import com.andmar.weather.data.WeatherRepository
import com.andmar.weather.data.LocationItem
import com.andmar.weather.location.LocationClient

class AddLocationViewModel(
    private val locationClient: LocationClient,
    private val weatherService: WeatherService,
    private val weatherDataStore: WeatherDataStore,
    private val weatherRepository: WeatherRepository
): ViewModel() {

    var addLocationUiState by mutableStateOf(AddLocationUiState())
        private set
    
    fun updateAddLocationUiState(addLocationState: AddLocationUiState) {
        addLocationUiState = addLocationUiState.copy(
            locationDetails = addLocationState.locationDetails,
            isShowButton = showButton(addLocationState.locationDetails),
            warningDialog = addLocationState.warningDialog
        )
    }
    
    fun saveLocation(location: String) = viewModelScope.launch {
        weatherDataStore.updateLocation(location)
    }
    
    var locationListState by mutableStateOf(LocationListState())
        private set
    
    fun getLocation() = viewModelScope.launch {
        locationListState = LocationListState(
            weatherService.weatherApi.getLocation(location = addLocationUiState.locationDetails.location)
        )
    }
    
    fun findLocation() = viewModelScope.launch {
        val location = locationClient.getLocation(1000)
            .filterNotNull()
            .first()
           
        if(location == "Missing location permission") {
            addLocationUiState = AddLocationUiState(
                warningDialog = true
            )
        } else {
            locationListState = LocationListState(
                weatherService.weatherApi.getLocation(
                    location = location
                )
            )
        }
    }
    
    fun saveLocationItem(location: Location) = viewModelScope.launch {
        weatherRepository.insertLocationItem(
            location.toLocationItem()
        )
    }
    
    private fun showButton(locationDetails: LocationDetails): Boolean {
        return if(locationDetails.location.isNotBlank()) true else false
    }
}

data class LocationListState(val locationList: List<Location> = emptyList())

data class AddLocationUiState(
    val locationDetails: LocationDetails = LocationDetails(),
    val location: String = "",
    val isShowButton: Boolean = false,
    val warningDialog: Boolean = false
)

data class LocationDetails(
    val location: String = ""
)

fun Location.toLocationItem(): LocationItem = LocationItem(
    name = name,
    region = region,
    country = country,
    lat = lat,
    lon = lon,
    url = url
)
