package com.andmar.weather.rest

data class Weather(
    val location: Location = Location(),
    val current: Current = Current(),
    val forecast: Forecast = Forecast(),
    val alerts: Alerts = Alerts()
) 

data class Current(
    val last_updated_epoch: Long = 0,
    val last_updated: String = "",
    val temp_c: Double = 0.0,
    val temp_f: Double = 0.0,
    val condition: Condition = Condition(),
    val wind_mph: Double = 0.0,
    val wind_kph: Double = 0.0,
    val wind_degree: Int = 0,
    val wind_dir: String = "",
    val pressure_mb: Double = 0.0,
    val pressure_in: Double = 0.0,
    val precip_mm: Double = 0.0,
    val precip_in: Double = 0.0,
    val humidity: Int = 0,
    val cloud: Int = 0,
    val feelslike_c: Double = 0.0,
    val feelslike_f: Double = 0.0,
    val windchill_c: Double = 0.0,
    val windchill_f: Double = 0.0,
    val heatindex_c: Double = 0.0,
    val heatindex_f: Double = 0.0,
    val dewpoint_c: Double = 0.0,
    val dewpoint_f: Double = 0.0,
    val vis_km: Double = 0.0,
    val vis_miles: Double = 0.0,
    val uv: Double = 0.0,
    val gust_mph: Double = 0.0,
    val gust_kph: Double = 0.0,
    val air_quality: AirQuality = AirQuality()
)

data class Forecast(
    val forecastday: List<ForecastDay> = listOf<ForecastDay>()
)

data class ForecastDay(
    val date: String = "",
    val date_epoch: Long = 0,
    val day: Day = Day(),
    val uv: Double = 0.0,
    val astro: Astro = Astro(),
    val hour: List<Hour> = listOf<Hour>()
)

data class Day(
    val maxtemp_c: Double = 0.0,
    val maxtemp_f: Double = 0.0,
    val mintemp_c: Double = 0.0,
    val mintemp_f: Double = 0.0,
    val avgtemp_c: Double = 0.0,
    val avgtemp_f: Double = 0.0,
    val maxwind_mph: Double = 0.0,
    val maxwind_kph: Double = 0.0,
    val totalprecip_mm: Double = 0.0,
    val totalprecip_in: Double = 0.0,
    val totalshow_cm: Double = 0.0,
    val avgvis_km: Double = 0.0,
    val avgvis_miles: Double = 0.0,
    val avghumidity: Double = 0.0,
    val daily_will_it_rain: Int = 0,
    val daily_chance_of_rain: Int = 0,
    val daily_will_it_snow: Int = 0,
    val daily_chance_of_snow: Int = 0,
    val condition: Condition = Condition(),
    val uv: Double = 0.0
)

data class AirQuality(
    val co: Double = 0.0,
    val no2: Double = 0.0,
    val o3: Double = 0.0,
    val so2: Double = 0.0,
    val pm2_5: Double = 0.0, 
    val pm10: Double = 0.0,
    val us_epa_index: Int = 0,
    val gb_defra_index: Int = 0
)

data class Astro(
    val sunrise: String = "",
    val sunset: String = "",
    val moonrise: String = "",
    val moonset: String = "",
    val moon_phase: String = "",
    val moon_illumination: Int = 0,
    val is_moon_up: Int = 0,
    val is_sun_up: Int = 0
)

data class Hour(
    val time_epoch: Long = 0,
    val time: String = "",
    val temp_c: Double = 0.0,
    val temp_f: Double = 0.0,
    val condition: Condition = Condition(),
    val wind_mph: Double = 0.0,
    val wind_kph: Double = 0.0,
    val wind_degree: Int = 0,
    val wind_dir: String = "",
    val pressure_mb: Double = 0.0,
    val pressure_in: Double = 0.0,
    val precip_mm: Double = 0.0,
    val precip_in: Double = 0.0,
    val show_cm: Double = 0.0,
    val humidity: Int = 0,
    val cloud: Int = 0,
    val feelslike_c: Double = 0.0,
    val feelslike_f: Double = 0.0,
    val windchill_c: Double = 0.0,
    val windchill_f: Double = 0.0,
    val heatindex_c: Double = 0.0,
    val heatindex_f: Double = 0.0,
    val dewpoint_c: Double = 0.0,
    val dewpoint_f: Double = 0.0,
    val will_it_rain: Int = 0,
    val chance_of_rain: Int = 0,
    val will_it_snow: Int = 0,
    val chance_of_snow: Int = 0,
    val vis_km: Double = 0.0,
    val vis_miles: Double = 0.0,
    val gust_mph: Double = 0.0,
    val gust_kph: Double = 0.0,
    val uv: Double = 0.0
)

data class Condition(
    val text: String = "",
    val icon: String = "",
    val code: Int = 0
)

data class Alerts(
    val alert: List<Alert> = emptyList()
)

data class Alert(
    val category: String = "",
    val event: String = "",
    val desc: String = ""
)

data class Location(
    val name: String = "",
    val region: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val tz_id: String = "",
    val localtime_epoch: Long = 0,
    val localtime: String = "",
    val url: String = ""
)