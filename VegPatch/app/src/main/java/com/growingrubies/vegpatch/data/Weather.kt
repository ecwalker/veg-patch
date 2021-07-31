package com.growingrubies.vegpatch.data

import java.io.Serializable

data class Weather(
    val timeStamp: Int?,
    val minTemp: Double,
    val maxTemp: Double
) : Serializable