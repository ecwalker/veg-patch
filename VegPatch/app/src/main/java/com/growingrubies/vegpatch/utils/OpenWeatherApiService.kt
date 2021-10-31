package com.growingrubies.vegpatch.utils

import com.growingrubies.vegpatch.BuildConfig.API_KEY
import com.growingrubies.vegpatch.utils.Constants.BASE_URL
import com.growingrubies.vegpatch.utils.Constants.EXCLUDE
import com.growingrubies.vegpatch.utils.Constants.UNITS
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
    @GET("data/2.5/onecall?lat=$londonLat&lon=$londonLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getLondonWeather():
            String

    @GET("data/2.5/onecall?lat=$brightonLat&lon=$brightonLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getBrightonWeather():
            String

    @GET("data/2.5/onecall?lat=$oxfordLat&lon=$oxfordLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getOxfordWeather():
            String

    @GET("data/2.5/onecall?lat=$cambridgeLat&lon=$cambridgeLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getCambridgeWeather():
            String

    @GET("data/2.5/onecall?lat=$southamptonLat&lon=$southamptonLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getSouthamptonWeather():
            String

    @GET("data/2.5/onecall?lat=$plymouthLat&lon=$plymouthLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getPlymouthWeather():
            String

    @GET("data/2.5/onecall?lat=$cardiffLat&lon=$cardiffLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getCardiffWeather():
            String

    @GET("data/2.5/onecall?lat=$bristolLat&lon=$bristolLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getBristolWeather():
            String

    @GET("data/2.5/onecall?lat=$liverpoolLat&lon=$liverpoolLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getLiverpoolWeather():
            String

    @GET("data/2.5/onecall?lat=$sheffieldLat&lon=$sheffieldLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getSheffieldWeather():
            String

    @GET("data/2.5/onecall?lat=$manchesterLat&lon=$manchesterLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getManchesterWeather():
            String

    @GET("data/2.5/onecall?lat=$leedsLat&lon=$leedsLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getLeedsWeather():
            String

    @GET("data/2.5/onecall?lat=$newcastleLat&lon=$newcastleLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getNewcastleWeather():
            String

    @GET("data/2.5/onecall?lat=$glasgowLat&lon=$glasgowLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getGlasgowWeather():
            String

    @GET("data/2.5/onecall?lat=$edinburghLat&lon=$edinburghLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getEdinburghWeather():
            String

    @GET("data/2.5/onecall?lat=$aberdeenLat&lon=$aberdeenLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getAberdeenWeather():
            String

    @GET("data/2.5/onecall?lat=$invernessLat&lon=$invernessLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getInvernessWeather():
            String

    @GET("data/2.5/onecall?lat=$belfastLat&lon=$belfastLong&exclude=$EXCLUDE&units=$UNITS&appid=$API_KEY")
    suspend fun getBelfastWeather():
            String
}

//OpenWeather API object to expose retrofit to rest of system
object OpenWeatherApi {
    val retrofitService : OpenWeatherApiService by lazy {
        retrofit.create(OpenWeatherApiService::class.java)
    }
}