package com.example.iram.weatherapp.Interfaces

import com.example.iram.weatherapp.OpenWeatherMap.openWeatherMapAPILocation


interface weatherByLocationInterface {
    fun getWeatherByLocation(result: openWeatherMapAPILocation)
}