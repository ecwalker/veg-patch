package com.growingrubies.vegpatch.utils

import android.app.NotificationManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.growingrubies.vegpatch.data.Weather
import com.growingrubies.vegpatch.data.dto.Result
import com.growingrubies.vegpatch.data.dto.WeatherDTO
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.PlantLocalRepository
import com.growingrubies.vegpatch.data.local.WeatherDatabaseDao
import retrofit2.HttpException

class RefreshDataWorker(private val plantDao: PlantDatabaseDao,
                        private val weatherDao: WeatherDatabaseDao,
                        appContext: Context,
                        params: WorkerParameters
):
    CoroutineWorker(appContext, params) {

    val notificationManager = appContext
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        val repository = PlantLocalRepository(plantDao, weatherDao)
        lateinit var weatherForecast: Weather

        return try {
            repository.refreshWeatherForecast()
            when (val weatherResult = repository.getWeatherForecast()) {
                is com.growingrubies.vegpatch.data.dto.Result.Success<*> -> {
                    val data = weatherResult.data as WeatherDTO
                    weatherForecast = Weather(
                        data.timeStamp,
                        data.minTemp,
                        data.maxTemp
                    )
                }
            }
            if (weatherForecast.maxTemp >= 25 || weatherForecast.minTemp <= 0) {
                notificationManager.sendNotification(applicationContext, weatherForecast)
            }
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }

    }



    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}