package com.example.iram.weatherapp.OpenWeatherMap

import android.support.v7.app.AppCompatActivity
import com.example.iram.weatherapp.Interfaces.HttpResponse
import com.example.iram.weatherapp.Interfaces.weatherByLocationInterface
import com.example.iram.weatherapp.Interfaces.weatherByNameInterface
import com.example.iram.weatherapp.Util.Network
import com.google.gson.Gson
import java.net.URLEncoder

class openWeatherMap(var activity:AppCompatActivity){
    private val URL_BASE="http://api.openweathermap.org/data/2.5/"
    private val API_ID="&appid=00be396ea806be96732f1beffcf2f828"
    fun getWeatherByName(name:String, unit:String, weatherByNameInterface:weatherByNameInterface){
        val network= Network(activity)
        val name=URLEncoder.encode(name, "UTF-8")
        val method="weather?q=$name"
        val url="$URL_BASE$method$API_ID$unit"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                var gson= Gson()
                var objectResonse=gson.fromJson(response, openWeatherMapAPIName::class.java)

                weatherByNameInterface.getWeatherByName(objectResonse)
            }
        })
    }
    fun getWeatherByLocation(lat:String, lon:String, weatherByLocationInterface:weatherByLocationInterface){
        val network= Network(activity)
        val method="find?lat=$lat&lon=$lon"
        val url="$URL_BASE$method$API_ID"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                var gson= Gson()
                var objectResonse=gson.fromJson(response, openWeatherMapAPILocation::class.java)
                weatherByLocationInterface.getWeatherByLocation(objectResonse)
            }
        })
    }
}