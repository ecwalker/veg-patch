package com.growingrubies.vegpatch.settings

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.WeatherDatabaseDao

class SettingsViewModel(val plantDao: PlantDatabaseDao,
                        val weatherDao: WeatherDatabaseDao,
                        application: Application
): AndroidViewModel(application) {

    /**
     * Functions to validate and save location data as a SharedPreference, then navigate to MainActivity
     */

    fun validateLocationEntered(latitude: String, longitude: String, context: Context): Boolean {
        //Logic to validate lat and long: Ensure both EditTexts are not empty
        if (latitude.isNotEmpty() && longitude.isNotEmpty()) {
            return true
        } else {
            val noLocationEntered = Resources.getSystem().getString(R.string.no_location_entered)
            Toast.makeText(context, noLocationEntered, Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun saveSharedPreferences(latitude: String, longitude: String, activity: AppCompatActivity) {
        //Save latitude and longitude to SharedPreferences
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        val latitudeKey = Resources.getSystem().getString(R.string.latitude_key)
        val longitudeKey = Resources.getSystem().getString(R.string.longitude_key)
        with (sharedPref.edit()) {
            putString(latitudeKey, latitude)
            putString(longitudeKey, longitude)
            apply()
        }
    }
}