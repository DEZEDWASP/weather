package com.andmar.weather.ui.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
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
import com.andmar.weather.rest.Weather
import com.andmar.weather.data.WeatherDataStore
import com.andmar.weather.data.WeatherRepository
import com.andmar.weather.ui.settings.Settings

class SettingsViewModel(
    private val weatherDataStore: WeatherDataStore,
): ViewModel() {

    var settingsUiState by mutableStateOf(SettingsUiState())
        private set
        
    init {
        viewModelScope.launch {
            settingsUiState = SettingsUiState(
                settings = weatherDataStore.getSettings
                    .filterNotNull()
                    .first()
            )
        }
    }     
    
    fun updateSettingsUiState(settingsState: SettingsUiState) = viewModelScope.launch {
        weatherDataStore.saveSettings(settingsState.settings)
        settingsUiState = settingsState
    }

}

data class SettingsUiState(
    val settings: Settings = Settings(),
    val bottomSheetState: BottomSheetState = BottomSheetState()
)

data class Settings(
    val windMeasure: String = "",
    val tempMeasure: String = "",
    val pressureMeasure: String = "",
    val precipMeasure: String = ""
)

data class BottomSheetState(
    val tempMenuSheet: Boolean = false,
    val windMenuSheet: Boolean = false,
    val pressureMenuSheet: Boolean = false
)
