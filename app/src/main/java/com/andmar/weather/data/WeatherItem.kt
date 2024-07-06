package com.andmar.weather.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ColumnInfo
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "location_item")
data class LocationItem(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int = 0,
    val id: Long = 0,
    val name: String = "",
    val region: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val url: String = ""
)