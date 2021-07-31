package com.growingrubies.vegpatch.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "weather_table")
data class WeatherDTO(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "time_stamp") val timeStamp: Int?,
    @ColumnInfo(name = "min_temp") val minTemp: Double,
    @ColumnInfo(name = "max_temp") val maxTemp: Double
)