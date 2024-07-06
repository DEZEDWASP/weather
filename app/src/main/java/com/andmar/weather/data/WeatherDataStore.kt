package com.andmar.weather.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import com.andmar.weather.ui.settings.Settings

class WeatherDataStore(
    private val dataStore: DataStore<Preferences>
) {
    
    private companion object {
        val LOCATION = stringPreferencesKey("location")
        
        val WIND_MEASURE = stringPreferencesKey("wind_measure")
        val TEMP_MEASURE = stringPreferencesKey("temp_measure")
        val PRESSURE_MEASURE = stringPreferencesKey("pressure_measure")
        val RECIP_MEASURE = stringPreferencesKey("recip_measure")
    }
    
    val getLocation = dataStore.data
        .map { preferences -> 
            return@map preferences[LOCATION] ?: ""
        }
    
    suspend fun updateLocation(location: String) {
        dataStore.edit { preferences ->
            preferences[LOCATION] = location
        }
    }
    
    val getSettings = dataStore.data
        .map { preferences ->
            return@map Settings(
                windMeasure = preferences[WIND_MEASURE] ?: "km/h",
                tempMeasure = preferences[TEMP_MEASURE] ?: "°C",
                pressureMeasure = preferences[PRESSURE_MEASURE] ?: "mb",
                precipMeasure = preferences[RECIP_MEASURE] ?: "mm"
            )
        }
    
    suspend fun saveSettings(settings: Settings) {
        dataStore.edit { preferences ->
            preferences[WIND_MEASURE] = settings.windMeasure
            preferences[TEMP_MEASURE] = settings.tempMeasure
            preferences[PRESSURE_MEASURE] = settings.pressureMeasure
            preferences[RECIP_MEASURE] = settings.precipMeasure
        }
    }
}