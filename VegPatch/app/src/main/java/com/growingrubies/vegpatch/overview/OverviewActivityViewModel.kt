package com.growingrubies.vegpatch.overview

import android.app.Application
import androidx.lifecycle.*
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.Weather
import com.growingrubies.vegpatch.data.dto.PlantDTO
import com.growingrubies.vegpatch.data.dto.Result
import com.growingrubies.vegpatch.data.dto.WeatherDTO
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.PlantList
import com.growingrubies.vegpatch.data.local.PlantLocalRepository
import com.growingrubies.vegpatch.data.local.WeatherDatabaseDao
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.collections.ArrayList

class OverviewActivityViewModel(
    val plantDao: PlantDatabaseDao,
    val weatherDao: WeatherDatabaseDao,
    application: Application): AndroidViewModel(application) {

    //Encapsulated LiveData

    private val _plantList = MutableLiveData<List<Plant>>()
    val plantList: LiveData<List<Plant>>
        get() = _plantList

    private val _weatherForecast = MutableLiveData<Weather>()
    val weatherForecast: LiveData<Weather>
        get() = _weatherForecast

    private val _navigateToPlantDetail = MutableLiveData<Long>()
    val navigateToPlantDetail: LiveData<Long>
        get() = _navigateToPlantDetail

    //Use repository instead of ViewModel directly
    private val plantRepository = PlantLocalRepository(plantDao, weatherDao)

    init {
        setPlantList()
        getPlantlist()
        getWeatherForecast()
    }

    /**
     * Functions interacting with repository for database
     */
    //TODO: Change the way the data is set...
    private fun setPlantList() {
        Timber.i("setPlantList called")
        val tempPlantList = PlantList
        var dbSize: Int? = null
        viewModelScope.launch {
            when (val result = plantRepository.checkNumPlants()) {
                is Result.Success<*> -> {
                    result
                    dbSize = result.data as Int
                }
                is Result.Error -> {
                    Timber.i("Error: ${result.message}")
                }
            }
            if (dbSize != tempPlantList.size && dbSize != null) {
                for (plant in tempPlantList) {
                    plantRepository.insert(
                        PlantDTO(plant.id,
                            plant.name,
                            plant.icon,
                            plant.annual,
                            plant.frostHardy,
                            plant.sowDate,
                            plant.plantDate,
                            plant.harvestDate,
                            plant.active)
                    )
                }
            }


        }
    }

    private fun getPlantlist() {
        Timber.i("getPlantList called")
        viewModelScope.launch {
            val result = plantRepository.getActivePlants()
            when (result) {
                is Result.Success<*> -> {
                    val dataList = ArrayList<Plant>()
                    dataList.addAll((result.data as List<PlantDTO>).map { plant ->
                        //map the reminder data from the DB to the be ready to be displayed on the UI
                        Plant(
                            plant.id,
                            plant.name,
                            plant.icon,
                            plant.annual,
                            plant.frostHardy,
                            plant.sowDate,
                            plant.plantDate,
                            plant.harvestDate,
                            plant.active
                        )
                    })
                    _plantList.value = dataList
                }
                is Result.Error -> {
                    Timber.i("getPlantList failed ${result.message}")
                }
            }

        }
    }

    /**
     * Navigation functions
     */

    fun onPlantClicked(id: Long) {
        _navigateToPlantDetail.value = id
    }

    /**
     * Function to get weather forecast form database
     */

    private fun getWeatherForecast() {
        viewModelScope.launch {
            try {
                val result = plantRepository.getWeatherForecast()
                when (result) {
                    is Result.Success<*> -> {
                        val data = result.data as WeatherDTO
                        _weatherForecast.value = Weather(
                            data.timeStamp,
                            data.minTemp,
                            data.maxTemp
                        )
                        Timber.i("Weather forecast is ${_weatherForecast.value}")

                    }
                    is Result.Error -> {
                        Timber.i("Result object error: ${result.message}")
                    }
                }
            } catch (e: Exception) {
                Timber.i("Exception: ${e.message}")
            }

        }
    }

}