package com.andmar.weather.location

import kotlinx.coroutines.flow.Flow

interface LocationClient {

    fun getLocation(interval: Long): Flow<String> 
    
    fun getLastLocation(): Flow<String>
    
    class LocationException(message: String): Exception()
}
