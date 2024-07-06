package com.andmar.weather.rest

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import kotlinx.coroutines.flow.Flow

const val API_KEY = "ca0bb09446b74820915130204240401"

interface WeatherApi {

    @GET("v1/forecast.json")
    suspend fun getWeather(
        @Query("key") key: String = API_KEY,
        @Query("q") location: String, 
        @Query("days") days: Int = 3,
        @Query("aqi") aqi: String = "yes",
        @Query("alerts") alerts: String = "yes",
        @Query("lang") lang: String = "ru"
    
    ): Weather
    
    @GET("v1/search.json")
    suspend fun getLocation(
        @Query("key") key: String = API_KEY,
        @Query("q") location: String,
        @Query("lang") lang: String = "ru"
    
    ): List<Location> //58.8574137 50.4350749
}
