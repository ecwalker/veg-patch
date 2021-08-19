package com.growingrubies.vegpatch.utils

import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.Weather
import com.growingrubies.vegpatch.data.dto.PlantDTO
import com.growingrubies.vegpatch.data.dto.Result
import com.growingrubies.vegpatch.data.dto.WeatherDTO
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.PlantLocalRepository
import com.growingrubies.vegpatch.data.local.WeatherDatabaseDao
import retrofit2.HttpException
import timber.log.Timber

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
        lateinit var activePlantList: List<Plant>
        lateinit var currentWeatherCode: WeatherCodes
        var currentWeatherString: String? = null

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
            when (val plantListResult = repository.getActivePlants()) {
                is com.growingrubies.vegpatch.data.dto.Result.Success<*> -> {
                    val dataList = ArrayList<Plant>()
                    dataList.addAll((plantListResult.data as List<PlantDTO>).map { plant ->
                        //map the reminder data from the DB to the be ready to be displayed on the UI
                        Plant(
                            plant.id,
                            plant.name,
                            plant.category,
                            plant.icon,
                            plant.isAnnual,
                            plant.isFrostHardy,
                            plant.isGreenhousePlant,
                            plant.sowDate,
                            plant.plantDate,
                            plant.harvestDate,
                            plant.isActive
                        )
                    })
                    activePlantList = dataList
                }
            }

            var isAnyPlantNotFrostHardy = false
            activePlantList.let{
                for (plant in it) {
                    if (!plant.isFrostHardy) {
                        isAnyPlantNotFrostHardy = true
                        break
                    }
                }
            }

            if (weatherForecast.maxTemp >= 25 && activePlantList.isNotEmpty()) {
                currentWeatherCode = WeatherCodes.HeatWarning
                currentWeatherString = setWeatherString(currentWeatherCode)
            } else if (weatherForecast.minTemp <= 0 && isAnyPlantNotFrostHardy) {
                currentWeatherCode = WeatherCodes.ColdWarning
                currentWeatherString = setWeatherString(currentWeatherCode)
            }

            //Send notification if there is a weather warning
            currentWeatherString?.let {
                notificationManager.sendNotification(applicationContext, it)
            } ?: Timber.i("Weather checked, no warning or notification sent")

            Result.success()

        } catch (e: HttpException) {
            Result.retry()
        }

    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    private fun setWeatherString(weatherCode: WeatherCodes): String {
        return when (weatherCode) {
            is WeatherCodes.NoWarning -> Resources.getSystem().getString(R.string.no_weather_warning)
            is WeatherCodes.ColdWarning -> Resources.getSystem().getString(R.string.cold_weather_warning)
            is WeatherCodes.HeatWarning -> Resources.getSystem().getString(R.string.heat_weather_warning)
            is WeatherCodes.Unset -> Resources.getSystem().getString(R.string.weather_warning_unset)
        }
    }
}