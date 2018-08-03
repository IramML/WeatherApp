package com.example.iram.weatherapp.OpenWeatherMap

import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.iram.weatherapp.Interfaces.HttpResponse
import com.example.iram.weatherapp.Interfaces.weatherByLocationInterface
import com.example.iram.weatherapp.Interfaces.weatherByNameInterface
import com.example.iram.weatherapp.Util.Network
import com.google.gson.Gson
import java.net.URLEncoder

class openWeatherMap(var activity:AppCompatActivity){
    private val URL_BASE="http://api.openweathermap.org/"
    private val VERSION="2.5/"
    private val API_ID="&appid=00be396ea806be96732f1beffcf2f828"
    fun getWeatherByName(name:String, unit:String, weatherByNameInterface:weatherByNameInterface){
        val network= Network(activity)
        val name=URLEncoder.encode(name, "UTF-8")
        val section="data/"
        val method="weather?q=$name"
        var url="$URL_BASE$section$VERSION$method$API_ID$unit"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                var gson= Gson()
                var objectResonse=gson.fromJson(response, openWeatherMapAPIName::class.java)
                if(!objectResonse.name.isNullOrEmpty()) {
                    val nameCity = objectResonse.name!!
                    val urlImage = makeIconURL(objectResonse.weather?.get(0)?.icon!!)
                    val status = objectResonse.weather?.get(0)?.main!!
                    val description = objectResonse.weather?.get(0)?.description!!
                    val temperature = objectResonse.main?.temp!!
                    val tempMin = objectResonse.main?.temp_min!!
                    val tempMax = objectResonse.main?.temp_max!!
                    weatherByNameInterface.getWeatherByName(nameCity, urlImage, status, description, temperature, tempMin, tempMax)
                }else{
                    Toast.makeText(activity.applicationContext, "Could not get Weather data", Toast.LENGTH_SHORT).show()
                    activity.finish()
                }
            }
        })
    }
    fun getWeatherByLocation(lat:String, lon:String, weatherByLocationInterface:weatherByLocationInterface){
        val network= Network(activity)
        val section="data/"
        val method="find?"
        val args="lat=$lat&lon=$lon"
        val url="$URL_BASE$section$VERSION$method$args$API_ID"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(response: String) {
                var gson= Gson()
                var objectResonse=gson.fromJson(response, openWeatherMapAPILocation::class.java)
                weatherByLocationInterface.getWeatherByLocation(objectResonse)
            }
        })
    }
    private fun makeIconURL(icon:String):String{
        val secion="img/w/"
        val url="$URL_BASE$secion$icon.png"
        return url
    }
}