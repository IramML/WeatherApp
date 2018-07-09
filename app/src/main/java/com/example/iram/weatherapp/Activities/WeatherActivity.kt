package com.example.iram.weatherapp.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.iram.weatherapp.R
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

class WeatherActivity : AppCompatActivity() {
    var nameCity:String?=null
    var lat:String?=null
    var lon:String?=null

    var toolbar:Toolbar?=null
    var ivStatus:ImageView?=null
    var tvCity:TextView?=null
    var tvTemperature:TextView?=null
    var tvStatus:TextView?=null
    var tvDescription:TextView?=null
    var metric=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        toolbar=findViewById(R.id.actionBarWeather)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        ivStatus=findViewById(R.id.ivStatus)
        tvCity=findViewById(R.id.tvCity)
        tvStatus=findViewById(R.id.tvStatus)
        tvTemperature=findViewById(R.id.tvTemperature)
        tvDescription=findViewById(R.id.tvDescription)

        nameCity=intent.getStringExtra("CITY")

        if (nameCity!=null){
            tvCity?.text=nameCity
            var unit:String?=intent.getStringExtra("UNIT")
            var url: String = "http://api.openweathermap.org/data/2.5/weather?q=$nameCity&appid=00be396ea806be96732f1beffcf2f828"
            if (unit!=null){
                url+="&units=$unit"
                metric=true
            }
            getWeatherData(url)
        }else{
            var unit:String?=intent.getStringExtra("UNIT_MAP")
            lon=intent.getStringExtra("LON")
            lat=intent.getStringExtra("LAT")
            var url:String="http://api.openweathermap.org/data/2.5/find?lat=$lat&lon=$lon&appid=00be396ea806be96732f1beffcf2f828"
            if (unit!=null){
                url+="&units=$unit"
                metric=true
            }
            getWeatherDataByLocation(url)
        }



    }
    private fun getWeatherDataByLocation(url: String){
        val client=OkHttpClient()
        val request=okhttp3.Request.Builder().url(url).build()
        client.newCall(request).enqueue(object:okhttp3.Callback{
            override fun onResponse(call: Call?, response: Response?) {
                val result=response?.body()?.string()

                val gson= Gson()
                val responseGson=gson.fromJson(result, com.example.iram.weatherapp.Gson.ResultMap::class.java)
                this@WeatherActivity.runOnUiThread {
                    tvCity?.text=responseGson.list?.get(0)!!.name
                    if (metric){
                        tvTemperature?.text="${responseGson.list?.get(0)!!.main?.temp}°C"
                    }else{
                        tvTemperature?.text="${responseGson.list?.get(0)!!.main?.temp}°F"
                    }

                    tvStatus?.text=responseGson.list?.get(0)!!.weather?.get(0)!!.main
                    tvDescription?.text=responseGson.list?.get(0)!!.weather?.get(0)!!.description

                    var urlImg:String="http://openweathermap.org/img/w/${responseGson.list?.get(0)!!.weather?.get(0)!!.icon}.png"
                    Log.d("IMAGE", urlImg)
                    Glide.with(this@WeatherActivity).load(urlImg).into(ivStatus)
                }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                Toast.makeText(applicationContext,"Error in HTTP request", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getWeatherData(url:String){
        val client=OkHttpClient()
        val request=okhttp3.Request.Builder().url(url).build()
        client.newCall(request).enqueue(object:okhttp3.Callback{
            override fun onResponse(call: Call?, response: Response?) {
                val result=response?.body()?.string()

                    val gson= Gson()
                    val responseGson=gson.fromJson(result, com.example.iram.weatherapp.Gson.Result::class.java)
                this@WeatherActivity.runOnUiThread {
                    tvCity?.text=responseGson.name
                    if (metric){
                        tvTemperature?.text="${responseGson.main?.temp}°C"
                    }else{
                        tvTemperature?.text="${responseGson.main?.temp}°F"
                    }

                    tvStatus?.text=responseGson.weather?.get(0)!!.main
                    tvDescription?.text=responseGson.weather?.get(0)!!.description

                    var urlImg:String="http://openweathermap.org/img/w/${responseGson.weather?.get(0)!!.icon}.png"
                    Log.d("IMAGE", urlImg)
                    Glide.with(this@WeatherActivity).load(urlImg).into(ivStatus)
                }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                Toast.makeText(applicationContext,"Error in HTTP request", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
