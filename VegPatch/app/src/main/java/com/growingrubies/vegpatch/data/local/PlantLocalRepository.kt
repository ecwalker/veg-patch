package com.growingrubies.vegpatch.data.local

import android.app.NotificationManager
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.dto.PlantDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import com.growingrubies.vegpatch.data.dto.Result
import com.growingrubies.vegpatch.data.dto.WeatherDTO
import com.growingrubies.vegpatch.utils.OpenWeatherApi
import com.growingrubies.vegpatch.utils.parseOpenWeatherJson
import org.json.JSONObject

class PlantLocalRepository(private val plantDao: PlantDatabaseDao,
                           private val weatherDao: WeatherDatabaseDao) {

    /**
     * Functions to interact with PlantDao
     */

    suspend fun insert(plant: PlantDTO) {
        Timber.i("insert called")
        withContext(Dispatchers.IO) {
            try {
                plantDao.insert(plant)
            } catch (e: Exception) {
                Timber.i("Insert failed: ${e.message}")
            }
        }
    }


    suspend fun getAllPlants(): Result<List<PlantDTO>> = withContext(Dispatchers.IO) {
        Timber.i("getAllPlants called")
        return@withContext try {
            Result.Success(plantDao.getAll())
        } catch (e: Exception) {
            Timber.i("Error: ${e.message}")
            Result.Error(e.localizedMessage)
        }
    }

    suspend fun getCurrentPlant(id: Long): Result<PlantDTO> = withContext(Dispatchers.IO) {
        Timber.i("getCurrentPlant called")
        return@withContext try {
            Result.Success(plantDao.get(id))
        } catch (e: Exception) {
            Result.Error(e.localizedMessage)
        }
    }

    suspend fun getActivePlants(): Result<List<PlantDTO>> = withContext(Dispatchers.IO) {
        Timber.i("getAllPlants called")
        return@withContext try {
            Result.Success(plantDao.getActivePlants())
        } catch (e: Exception) {
            Timber.i("Error: ${e.message}")
            Result.Error(e.localizedMessage)
        }
    }

    suspend fun checkNumPlants(): Result<Int> = withContext(Dispatchers.IO) {
        Timber.i("checkNumPlants called")
        return@withContext try {
            Result.Success(plantDao.countNumPlants())
        } catch (e: Exception) {
            Timber.i("Error: ${e.message}")
            Result.Error(e.localizedMessage)
        }
    }

    /**
     * Functions to interact with WeatherDao
     */

    suspend fun refreshWeatherForecast() {
        Timber.i("refreshWeatherForecast called")
        withContext(Dispatchers.IO) {
            try {
                val stringResult = OpenWeatherApi.retrofitService.getWeather()
                Timber.i("OpenWeatherAPI returned: $stringResult")
                val result = parseOpenWeatherJson(JSONObject(stringResult))
                Timber.i("Parsed weather result: $result")
                weatherDao.insertWeather(
                    WeatherDTO(
                        result.timeStamp,
                        result.minTemp,
                        result.maxTemp
                    )
                )
            } catch (e: Exception) {
                Timber.i("Exception: ${e.message}")
            }
        }
    }

    suspend fun getWeatherForecast(): Result<WeatherDTO> = withContext(Dispatchers.IO) {
        Timber.i("getWeatherForecast called")
        return@withContext try {
            Result.Success(weatherDao.getWeather())
        } catch (e: Exception) {
            Timber.i("Error: ${e.message}")
            Result.Error(e.localizedMessage)
        }
    }

}