package com.andmar.weather.rest

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class WeatherService {

    val interceptor = HttpLoggingInterceptor()
    
  
   
   init {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
   }
    
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val weatherRest = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create()).build()
        
    val weatherApi = weatherRest.create(WeatherApi::class.java)
    
    var weather = Weather()
    
    suspend fun getWeather(location: String?): Weather {
        weather = weatherApi.getWeather(location = location as String)
        return weather
    }
}