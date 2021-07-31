package com.growingrubies.vegpatch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.growingrubies.vegpatch.data.dto.WeatherDTO

//TODO: properly define today in Query
const val TODAY = "2021-07-25"

@Dao
interface WeatherDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWeather(weather: WeatherDTO)

    @Query("SELECT * FROM weather_table ORDER BY time_stamp DESC LIMIT 1")
    suspend fun getWeather(): WeatherDTO

}