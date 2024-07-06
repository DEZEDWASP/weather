package com.andmar.weather.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationItem(locationItem: LocationItem)
    
    @Delete
    suspend fun deleteLocationItem(locationItem: LocationItem)
    
    @Query("SELECT * from location_item")
    fun getLocationItems(): Flow<List<LocationItem>>
}
