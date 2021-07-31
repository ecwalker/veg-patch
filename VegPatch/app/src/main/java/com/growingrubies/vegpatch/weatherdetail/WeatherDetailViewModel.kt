package com.growingrubies.vegpatch.weatherdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.WeatherDatabaseDao

class WeatherDetailViewModel(
    val plantDao: PlantDatabaseDao,
    val weatherDao: WeatherDatabaseDao,
    application: Application
): AndroidViewModel(application) {


}