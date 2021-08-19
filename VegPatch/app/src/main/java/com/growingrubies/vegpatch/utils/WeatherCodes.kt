package com.growingrubies.vegpatch.utils

sealed class WeatherCodes {
    object Unset: WeatherCodes()
    object NoWarning: WeatherCodes()
    object HeatWarning: WeatherCodes()
    object ColdWarning: WeatherCodes()
}