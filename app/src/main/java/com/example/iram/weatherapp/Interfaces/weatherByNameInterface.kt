package com.example.iram.weatherapp.Interfaces

import com.example.iram.weatherapp.OpenWeatherMap.openWeatherMapAPIName

interface weatherByNameInterface {
    fun getWeatherByName(result:openWeatherMapAPIName)
}