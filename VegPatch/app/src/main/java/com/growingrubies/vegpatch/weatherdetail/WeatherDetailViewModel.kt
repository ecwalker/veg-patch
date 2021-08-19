package com.growingrubies.vegpatch.weatherdetail

import android.app.Application
import android.content.res.Resources
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import com.growingrubies.vegpatch.R
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.data.Weather
import com.growingrubies.vegpatch.data.local.PlantDatabaseDao
import com.growingrubies.vegpatch.data.local.WeatherDatabaseDao
import com.growingrubies.vegpatch.utils.WeatherCodes
import timber.log.Timber
import java.util.*

class WeatherDetailViewModel(
    val plantDao: PlantDatabaseDao,
    val weatherDao: WeatherDatabaseDao,
    application: Application
): AndroidViewModel(application) {


    fun setWeatherImage(imgView: ImageView, currentWeatherString: String) {
        Timber.i("setWeatherImage called")

        //Set weather icon (assumes that no 3 day weather forecast will predict both sub-zero
        //temperatures and over 25C)
        val coldWarningString = "Cold weather is forecast. Some of your plants may need protecting."
        val heatWarningString = "Hot weather is forecast. Your plants may benefit from extra watering."
        if (currentWeatherString == coldWarningString) {
            imgView.setImageResource(R.drawable._26_cold)
            return
        } else if (currentWeatherString == heatWarningString) {
            imgView.setImageResource(R.drawable._27_hot)
            return
        } else {
            val rightNow = Calendar.getInstance()
            Timber.i("Calendar month: ${rightNow.get(Calendar.MONTH)}")
            when (rightNow.get(Calendar.MONTH)) {
                Calendar.JANUARY -> imgView.setImageResource(R.drawable._39_winter)
                Calendar.FEBRUARY -> imgView.setImageResource(R.drawable._39_winter)
                Calendar.MARCH -> imgView.setImageResource(R.drawable._36_spring)
                Calendar.APRIL -> imgView.setImageResource(R.drawable._36_spring)
                Calendar.MAY -> imgView.setImageResource(R.drawable._36_spring)
                Calendar.JUNE -> imgView.setImageResource(R.drawable._37_summer)
                Calendar.JULY -> imgView.setImageResource(R.drawable._37_summer)
                Calendar.AUGUST -> imgView.setImageResource(R.drawable._37_summer)
                Calendar.SEPTEMBER -> imgView.setImageResource(R.drawable.autumn)
                Calendar.OCTOBER -> imgView.setImageResource(R.drawable.autumn)
                Calendar.NOVEMBER -> imgView.setImageResource(R.drawable.autumn)
                Calendar.DECEMBER -> imgView.setImageResource(R.drawable._39_winter)
            }

        }
    }

}