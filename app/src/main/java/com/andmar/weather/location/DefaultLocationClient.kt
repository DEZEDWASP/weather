package com.andmar.weather.location

import android.os.Looper
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose

class DefaultLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
): LocationClient {

    @SuppressLint("MissingPermission")
    override fun getLocation(interval: Long): Flow<String> {
        return callbackFlow {
            if(!context.hasLocationPermission()) {
                throw LocationClient.LocationException("Missing location permission")
            }
            
            val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled =  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =  locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            
            if(!isGpsEnabled && !isNetworkEnabled) {
                throw LocationClient.LocationException("GPS is disabled")
            }
            
            val locationRequest = LocationRequest.create()
                .setInterval(interval)
                .setFastestInterval(interval)
                
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send("${location.latitude}, ${location.longitude}") }
                    }
                }
            }    
            
            client.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            
            awaitClose {
                client
            }
        }
    }
    
    override fun getLastLocation(): Flow<String> {
        return callbackFlow {
            client.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if(location != null) {
                        trySend("${location.latitude}, ${location.longitude}")
                    }
            }
            awaitClose {}
        }
    }
}
