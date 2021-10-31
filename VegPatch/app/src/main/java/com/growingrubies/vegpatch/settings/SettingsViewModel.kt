package com.growingrubies.vegpatch.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.PlantLocalRepository
import com.growingrubies.vegpatch.data.local.WeatherDatabaseDao
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class SettingsViewModel(val plantDao: PlantDatabaseDao,
                        val weatherDao: WeatherDatabaseDao,
                        application: Application
): AndroidViewModel(application) {

    //LiveData on navigation to save preferences and trigger db refresh
    private val _isNavigation = MutableLiveData<Boolean>()
    val isNavigation: LiveData<Boolean>
            get() = _isNavigation

    init {
        _isNavigation.value = false
    }
    //Use repository instead of ViewModel directly (for weather forecast refresh)
    private val plantRepository = PlantLocalRepository(plantDao, weatherDao)

    fun onNavigation() {
        _isNavigation.value = true
    }

    fun saveSharedPreferences(city: Any, activity: AppCompatActivity) {
        //Save selected city to SharedPreferences
        Timber.i("saveSharedPreferences called")
        val cityPref = activity.getString(R.string.preference_file_key)
        val cityKey = activity.getString(R.string.city_key)
        val sharedPref = activity.getSharedPreferences(cityPref, Context.MODE_PRIVATE)
        Timber.i("City key: $cityKey")
        with (sharedPref.edit()) {
            putString(cityKey, city.toString())
            apply()
        }

        refreshWeatherForecast(sharedPref, cityKey)
    }

    private fun refreshWeatherForecast(sharedPref: SharedPreferences, cityKey: String) {
        //Use new shared preference city to refresh db
        Timber.i("refreshWeatherForecast called")
        val sharedCity = sharedPref.getString(cityKey, null)
        Timber.i("Shared pref now: $sharedCity")
        viewModelScope.launch {
            try {
                plantRepository.refreshWeatherForecast(sharedCity)
            } catch (e: Exception) {
                Timber.i("Error: ${e.localizedMessage}")
            }
        }
    }
}