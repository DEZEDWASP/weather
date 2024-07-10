package com.andmar.weather.ui.weather

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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.andmar.weather.rest.WeatherService
import com.andmar.weather.rest.Weather
import com.andmar.weather.rest.Current
import com.andmar.weather.rest.Forecast
import com.andmar.weather.rest.ForecastDay
import com.andmar.weather.rest.Day
import com.andmar.weather.rest.Hour
import com.andmar.weather.rest.AirQuality
import com.andmar.weather.rest.Astro
import com.andmar.weather.rest.Alerts
import com.andmar.weather.rest.Alert
import com.andmar.weather.rest.Condition
import com.andmar.weather.rest.Location
import com.andmar.weather.data.WeatherDataStore
import com.andmar.weather.data.WeatherRepository
import com.andmar.weather.data.LocationItem
import com.andmar.weather.ui.settings.Settings
import com.andmar.weather.location.LocationClient

class WeatherViewModel(
    private val locationClient: LocationClient,
    private val weatherService: WeatherService,
    private val weatherDataStore: WeatherDataStore,
    private val weatherRepository: WeatherRepository
): ViewModel() {
    
    var weatherUiState by mutableStateOf(WeatherUiState())
        private set
    
    init {
       // getWeather()
    }
    
    fun updateWeatherUiState(weatherState: WeatherUiState) {
        weatherUiState = weatherState
    }
    
    fun getWeather() = viewModelScope.launch {
        val location: String? = weatherDataStore.getLocation.first()
        val lastLocation = locationClient.getLastLocation()
            .filterNotNull()
            .first()
            
        weatherUiState = weatherUiState.copy(
            weather = weatherService.getWeather(
                location = if(location as String == "") {
                    lastLocation
                } else {
                    location
                }
            ).toWeatherUi(weatherDataStore.getSettings
                .filterNotNull()
                .first()
            )
        )
    }
}

data class WeatherUiState(
    val weather: WeatherUi = WeatherUi()
)

data class WeatherUi(
    val location: LocationUi = LocationUi(),
    val current: CurrentUi = CurrentUi(),
    val forecast: ForecastUi = ForecastUi(),
    val alerts: AlertsUi = AlertsUi()
) 

data class CurrentUi(
    val last_updated: String = "",
    val temp: String = "",
    val condition: ConditionUi = ConditionUi(),
    val wind: String = "",
    val wind_dir: String = "",
    val pressure: String = "",
    val precip: String = "",
    val humidity: String = "",
    val feelslike: String = "",
    val uv: Double = 0.0,
    val gust: String = "",
    val air_quality: AirQualityUi = AirQualityUi()
)

data class ForecastUi(
    val forecastday: List<ForecastDayUi> = listOf<ForecastDayUi>()
)

data class ForecastDayUi(
    val date: String = "",
    val day: DayUi = DayUi(),
    val uv: Double = 0.0,
    val astro: AstroUi = AstroUi(),
    val hour: List<HourUi> = listOf<HourUi>()
)

data class DayUi(
    val maxtemp: String = "",
    val mintemp: String = "",
    val avgtemp: String = "",
    val maxwind: String = "",
    val totalprecip: String = "",
    val avghumidity: String = "",
    val daily_chance_of_rain: String = "",
    val daily_chance_of_snow: String = "",
    val condition: ConditionUi = ConditionUi(),
    val uv: Double = 0.0
)

data class AirQualityUi(
    val co: Double = 0.0,
    val no2: Double = 0.0,
    val o3: Double = 0.0,
    val so2: Double = 0.0,
    val pm2_5: Double = 0.0, 
    val pm10: Double = 0.0,
    val us_epa_index: Int = 0,
    val gb_defra_index: Int = 0
)

data class AstroUi(
    val sunrise: String = "",
    val sunset: String = "",
    val is_sun_up: Int = 0
)

data class HourUi(
    val time: String = "",
    val temp: String = "",
    val condition: ConditionUi = ConditionUi(),
    val wind: String = ""
)

data class ConditionUi(
    val text: String = "",
    val icon: String = "",
    val code: Int = 0
)

data class AlertsUi(
    val alert: List<AlertUi> = emptyList()
)

data class AlertUi(
    val category: String = "",
    val event: String = "",
    val desc: String = ""
)

data class LocationUi(
    val name: String = "",
    val region: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0
)

fun Weather.toWeatherUi(settings: Settings) = WeatherUi(
    location = this.location.toLocationUi(),
    current = this.current.toCurrentUi(settings),
    forecast = this.forecast.toForecastUi(settings),
    alerts = this.alerts.toAlertsUi()
)

fun Location.toLocationUi() = LocationUi(
    name = name,
    region = region,
    country = country,
    lat = lat,
    lon = lon
)

fun Current.toCurrentUi(settings: Settings) = CurrentUi(
    last_updated = last_updated,
    temp = if(settings.tempMeasure == "°C") "${this.temp_c}°" else "${this.temp_f}°",
    condition = this.condition.toConditionUi(),
    wind = if(settings.windMeasure == "m/h") "${this.wind_mph} m/h" else "${this.wind_kph} km/h",
    wind_dir = wind_dir,
    pressure = if(settings.pressureMeasure == "mb") "${this.pressure_mb} mb" else "${this.pressure_in}",
    precip = if(settings.precipMeasure == "mm") "${this.precip_mm} mm" else "${this.precip_in} m/h",
    humidity = "${this.humidity} %",
    feelslike = if(settings.tempMeasure == "°C") "${this.feelslike_c}°" else "${this.feelslike_f}°",
    uv = uv,
    gust = if(settings.windMeasure == "m/h") "${this.gust_mph} m/h" else "${this.gust_kph} km/h",
    air_quality = this.air_quality.toAirQualityUi()
)

fun Forecast.toForecastUi(settings: Settings) = ForecastUi(
    forecastday = ForecastDayListAsForecastDayUiList(this.forecastday, settings)
)

fun ForecastDayListAsForecastDayUiList(forecastDayList: List<ForecastDay>, settings: Settings): List<ForecastDayUi> {
    val forecastDayUiList = mutableListOf<ForecastDayUi>() 
    
    forecastDayList.forEach { item ->
        forecastDayUiList.add(item.toForecastDayUi(settings))
    }
    return forecastDayUiList.toList()
}

fun ForecastDay.toForecastDayUi(settings: Settings) = ForecastDayUi(
    date = date,
    day = this.day.toDayUi(settings),
    uv = uv,
    astro = this.astro.toAstroUi(),
    hour = HourListAsHourUiList(this.hour, settings)
)

fun HourListAsHourUiList(hourList: List<Hour>, settings: Settings): List<HourUi> {
    val hourUiList = mutableListOf<HourUi>()
    
    hourList.forEach { item ->
        hourUiList.add(item.toHourUi(settings))
    }
    return hourUiList.toList()
}

fun Day.toDayUi(settings: Settings) = DayUi(
    maxtemp = if(settings.tempMeasure == "°C") "${this.maxtemp_c}°" else "${this.maxtemp_f}°",
    mintemp = if(settings.tempMeasure == "°C") "${this.mintemp_c}°" else "${this.mintemp_f}°",
    avgtemp = if(settings.tempMeasure == "°C") "${this.avgtemp_c}°" else "${this.avgtemp_f}°",
    maxwind = if(settings.windMeasure == "m/h") "${this.maxwind_mph} m/h" else "${this.maxwind_kph} km/h",
    totalprecip = if(settings.precipMeasure == "mm") "${this.totalprecip_mm} mm" else "${this.totalprecip_in}",
    avghumidity = "${this.avghumidity} %",
    daily_chance_of_rain = "${this.daily_chance_of_rain} %",
    daily_chance_of_snow = "${this.daily_chance_of_snow} %",
    condition = this.condition.toConditionUi(),
    uv = uv
)

fun Hour.toHourUi(settings: Settings) = HourUi(
    time = time,
    temp = if(settings.tempMeasure == "°C") "${this.temp_c}°" else "${this.temp_f}°",
    condition = this.condition.toConditionUi(),
    wind = if(settings.windMeasure == "m/h") "${this.wind_mph} m/h" else "${this.wind_kph} km/h"
)

fun Condition.toConditionUi() = ConditionUi(
    text = text,
    icon = icon,
    code = code
)

fun AirQuality.toAirQualityUi() = AirQualityUi(
    co = co,
    no2 = no2,
    o3 = o3,
    so2 = so2,
    pm2_5 = pm2_5, 
    pm10 = pm10,
    us_epa_index = us_epa_index,
    gb_defra_index = gb_defra_index
)

fun Astro.toAstroUi() = AstroUi(
    sunrise = sunrise,
    sunset = sunset,
)

fun Alerts.toAlertsUi() = AlertsUi(
    alert = AlertListAsAlertUiList(this.alert)
)

fun AlertListAsAlertUiList(alertsList: List<Alert>): List<AlertUi>{
    val alertUiList = mutableListOf<AlertUi>()
    
    alertsList.forEach { item ->
        alertUiList.add(item.toAlertUi())
    }
    return alertUiList.toList()
}

fun Alert.toAlertUi() = AlertUi(
    category = category,
    event = event,
    desc = desc
)