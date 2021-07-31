package com.growingrubies.vegpatch.utils

import com.growingrubies.vegpatch.utils.Constants.API_KEY
import com.growingrubies.vegpatch.utils.Constants.BASE_URL
import com.growingrubies.vegpatch.utils.Constants.EXCLUDE
import com.growingrubies.vegpatch.utils.Constants.LAT
import com.growingrubies.vegpatch.utils.Constants.LONG
import com.growingrubies.vegpatch.utils.Constants.UNITS
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

//Retrofit Object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

//@GET method to get weather
interface OpenWeatherApiService {
    @GET("data/2.5/onecall?lat=$LAT&lon=$LONG&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getWeather():
            String
}

//OpenWeather API object to expose retrofit to rest of system
object OpenWeatherApi {
    val retrofitService : OpenWeatherApiService by lazy {
        retrofit.create(OpenWeatherApiService::class.java)
    }
}