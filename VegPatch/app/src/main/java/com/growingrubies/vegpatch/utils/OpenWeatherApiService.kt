package com.growingrubies.vegpatch.utils

import android.app.Application
import android.content.Context
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
import kotlin.coroutines.coroutineContext

//Retrofit Object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

//TODO: Retrieve SharedPreferences?? Need to get application context
//val context = Application()
//val sharedPref = Context.getSharedPreferences(Context.MODE_PRIVATE) ?: return
//
//var lat = 0
//var long = 0

//@GET method to get weather
interface OpenWeatherApiService {
    //TODO: Fix annotations must be compile time constant
    @GET("data/2.5/onecall?lat=$LAT&lon=$LONG&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    //@GET("data/2.5/onecall?lat=$lat&lon=$long&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getWeather():
            String
}

//OpenWeather API object to expose retrofit to rest of system
object OpenWeatherApi {
    val retrofitService : OpenWeatherApiService by lazy {
        retrofit.create(OpenWeatherApiService::class.java)
    }
}