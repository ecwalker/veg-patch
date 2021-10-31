package com.growingrubies.vegpatch.overview

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.Weather
import com.growingrubies.vegpatch.data.dto.PlantDTO
import com.growingrubies.vegpatch.data.dto.Result
import com.growingrubies.vegpatch.data.dto.WeatherDTO
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.PlantList
import com.growingrubies.vegpatch.data.local.PlantLocalRepository
import com.growingrubies.vegpatch.data.local.WeatherDatabaseDao
import com.growingrubies.vegpatch.utils.WeatherCodes
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.Calendar.*
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

    private val _dbReturns = MutableLiveData<Int>()
    val dbReturns: LiveData<Int>
        get() = _dbReturns

    private val _isAnyPlantNotFrostHardy = MutableLiveData<Boolean>()
    val isAnyPlantNotFrostHardy: LiveData<Boolean>
        get() = _isAnyPlantNotFrostHardy

    private val _currentWeatherCode = MutableLiveData<WeatherCodes>()
    val currentWeatherCode: LiveData<WeatherCodes>
        get() = _currentWeatherCode

    //Use repository instead of ViewModel directly
    private val plantRepository = PlantLocalRepository(plantDao, weatherDao)

    init {
        setPlantList()
        getPlantlist()
        _dbReturns.value = 0
        _isAnyPlantNotFrostHardy.value = false
        _currentWeatherCode.value = WeatherCodes.Unset
    }

    /**
     * Functions interacting with repository for database
     */
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
                            plant.category,
                            plant.icon,
                            plant.isAnnual,
                            plant.isFrostHardy,
                            plant.isGreenhousePlant,
                            plant.sowDate,
                            plant.plantDate,
                            plant.harvestDate,
                            plant.isActive)
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
                    _plantList.value = dataList
                    _dbReturns.value = _dbReturns.value!!.plus(1)
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

    fun getWeatherForecast(activity: AppCompatActivity) {
        viewModelScope.launch {
            var maxTries = 2
            var countTries = 0
            while (true) {
                try {
                    when (val result = plantRepository.getWeatherForecast()) {
                        is Result.Success<*> -> {
                            val data = result.data as WeatherDTO
                            _weatherForecast.value = Weather(
                                data.timeStamp,
                                data.minTemp,
                                data.maxTemp
                            )
                            Timber.i("Weather forecast is ${_weatherForecast.value}")
                            _dbReturns.value = _dbReturns.value!!.plus(1)
                            break
                        }
                        is Result.Error -> {
                            countTries = countTries++
                            Timber.i("Repository error on get WeatherForecast. Attempt $countTries")
                            if (countTries >= maxTries) {
                                break
                            }
                            else {
                                Timber.i("Result object error: ${result.message}")
                                val cityPref = activity.getString(R.string.preference_file_key)
                                val cityKey = activity.getString(R.string.city_key)
                                val sharedPref = activity.getSharedPreferences(cityPref, Context.MODE_PRIVATE)
                                plantRepository.refreshWeatherForecast(sharedPref.getString(cityKey, null))
                            }

                        }
                    }
                } catch (e: Exception) {
                    Timber.i("Exception: ${e.message}")
                }
            }


        }
    }

    /**
     * Functions to check SharedPreferences and prompt user to update if not set
     */

    fun checkSharedPreferences(activity: AppCompatActivity) {
        val cityPref = activity.getString(R.string.preference_file_key)
        val cityKey = activity.getString(R.string.city_key)
        val sharedPref = activity.getSharedPreferences(cityPref, Context.MODE_PRIVATE)
        val city = sharedPref.getString(cityKey, null)
        Timber.i("City key: $cityKey")
        Timber.i("Shared pref: ${sharedPref.getString(cityKey, null)}")
        if (city.isNullOrEmpty()) {
            val cityPrompt = activity.getString(R.string.city_not_set)
            Toast.makeText(activity, cityPrompt, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Function with logic to handle the weather forecast returned from database
     */

    fun setWeatherImage(imgView: ImageView, weather: Weather?, activePlantList: List<Plant>?) {
        Timber.i("setWeatherImage called")
        activePlantList?.let{
            for (plant in it) {
                if (!plant.isFrostHardy) {
                    _isAnyPlantNotFrostHardy.value = true
                    break
                }
            }
        } ?: Timber.i("activePlantList is empty")

        //Set weather icon (assumes that no 3 day weather forecast will predict both sub-zero
        //temperatures and over 25C)
        if (weather!!.minTemp <= 0 && isAnyPlantNotFrostHardy.value!!) {
            _currentWeatherCode.value = WeatherCodes.ColdWarning
            imgView.setImageResource(R.drawable._26_cold)
            return
        } else if (weather.maxTemp >= 25 && activePlantList!!.isNotEmpty()) {
            _currentWeatherCode.value = WeatherCodes.HeatWarning
            imgView.setImageResource(R.drawable._27_hot)
            return
        } else {
            _currentWeatherCode.value = WeatherCodes.NoWarning
            val rightNow = Calendar.getInstance()
            Timber.i("Calendar month: ${rightNow.get(MONTH) + 1}")
            when (rightNow.get(MONTH)) {
                JANUARY -> imgView.setImageResource(R.drawable._39_winter)
                FEBRUARY -> imgView.setImageResource(R.drawable._39_winter)
                MARCH -> imgView.setImageResource(R.drawable._36_spring)
                APRIL -> imgView.setImageResource(R.drawable._36_spring)
                MAY -> imgView.setImageResource(R.drawable._36_spring)
                JUNE -> imgView.setImageResource(R.drawable._37_summer)
                JULY -> imgView.setImageResource(R.drawable._37_summer)
                AUGUST -> imgView.setImageResource(R.drawable._37_summer)
                SEPTEMBER -> imgView.setImageResource(R.drawable.autumn)
                OCTOBER -> imgView.setImageResource(R.drawable.autumn)
                NOVEMBER -> imgView.setImageResource(R.drawable.autumn)
                DECEMBER -> imgView.setImageResource(R.drawable._39_winter)
            }

        }
    }

    /**
     * Function to force update weather forecast due to change of location SharedPreference
     */



}