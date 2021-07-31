package com.growingrubies.vegpatch.utils

import com.growingrubies.vegpatch.data.Weather
import org.json.JSONObject
import timber.log.Timber
import java.lang.Double.NaN

fun parseOpenWeatherJson(jsonResult: JSONObject): Weather {
    Timber.i("parseOpenWeatherJson called")

    val hourlyJson = jsonResult.getJSONArray("hourly")

    val temperatureList = ArrayList<Double>()
    var timeStamp: Int? = null

    for (i in 0 until hourlyJson.length()) {
        val weatherJson = hourlyJson.getJSONObject(i)
        temperatureList.add(weatherJson.getDouble("temp"))
        if (timeStamp == null) {
            timeStamp = weatherJson.getInt("dt")
        }
    }

    var minTempSoFar: Double = NaN
    var maxTempSoFar: Double = NaN
    for (i in 0 until temperatureList.size) {
        if (minTempSoFar.isNaN() || temperatureList[i] < minTempSoFar) {
            minTempSoFar = temperatureList[i]
        }
        if (maxTempSoFar.isNaN() || temperatureList[i] > maxTempSoFar) {
            maxTempSoFar = temperatureList[i]
        }
    }

    return Weather(timeStamp, minTempSoFar, maxTempSoFar)
}