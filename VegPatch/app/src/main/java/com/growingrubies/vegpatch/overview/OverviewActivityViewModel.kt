package com.growingrubies.vegpatch.overview

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Global.getString
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
import com.growingrubies.vegpatch.utils.Constants
import com.growingrubies.vegpatch.utils.WeatherCodes
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Month
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
        getWeatherForecast()
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

    private fun getWeatherForecast() {
        viewModelScope.launch {
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
                    }
                    is Result.Error -> {
                        Timber.i("Result object error: ${result.message}")
                        //TODO: Temp fix here, but will not load properly on fresh install
                        //If weather forecast is empty then force call refresh??
                        plantRepository.refreshWeatherForecast()
                    }
                }
            } catch (e: Exception) {
                Timber.i("Exception: ${e.message}")
            }

        }
    }

    /**
     * Functions to check SharedPreferences and prompt user to update if not set
     */

    fun checkSharedPreferences(activity: AppCompatActivity, context: Context) {

        //Retrieve SharedPreferences
        val latitudeKey = Resources.getSystem().getString(R.string.latitude_key)
        val longitudeKey = Resources.getSystem().getString(R.string.longitude_key)
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val latitudeSharedPref = sharedPref.getString(latitudeKey, Constants.LAT)
        val longitudeSharedPref = sharedPref.getString(longitudeKey, Constants.LONG)
        Timber.i("Shared Preferences retrieved: $latitudeSharedPref and $longitudeSharedPref")

        //Check whether SharedPreferences have been set
        val isLatitudeSet = latitudeSharedPref != Constants.LAT
        val isLongitudeSet = longitudeSharedPref != Constants.LONG
        if (!isLatitudeSet && !isLongitudeSet) {
            val setLocationReminder = Resources.getSystem().getString(R.string.set_location)
            Toast.makeText(context, setLocationReminder, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Function with logic to handle the weather forecast returned from database
     */

    fun setWeatherImage(imgView: ImageView, weather: Weather?, activePlantList: List<Plant>?) {
        Timber.i("setWeatherImage called")
        //var isAnyPlantNotFrostHardy = false
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
            Timber.i("Calendar month: ${rightNow.get(MONTH)}")
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