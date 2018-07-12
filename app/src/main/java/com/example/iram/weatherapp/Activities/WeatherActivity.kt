package com.example.iram.weatherapp.Activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.ArrayAdapter
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
    var context:Context=this
    var nameCity:String?=null
    var lat:String?=null
    var lon:String?=null

    var toolbar:Toolbar?=null
    var ivStatus:ImageView?=null
    var tvCity:TextView?=null
    var tvTemperature:TextView?=null
    var tvStatus:TextView?=null
    var tvDescription:TextView?=null
    var tvTempMin:TextView?=null
    var tvTempMax:TextView?=null
    var metric=false
    var map:Boolean=false
    var unit:String?=null
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
        tvTempMax=findViewById(R.id.tvTempMax)
        tvTempMin=findViewById(R.id.tvTempMin)

        nameCity=intent.getStringExtra("CITY")
        var url:String?="http://api.openweathermap.org/data/2.5/"
        if (nameCity!=null){
            url+="weather?q=$nameCity"
            map=false
        }else{
            lon=intent.getStringExtra("LON")
            lat=intent.getStringExtra("LAT")
            url+="find?lat=$lat&lon=$lon"
            map=true
        }
        unit=intent.getStringExtra("UNIT")
        if (unit!=null){
            url+="&units=$unit"
            metric=true
        }
        getWeatherData(url+"&appid=00be396ea806be96732f1beffcf2f828", map)
    }
    private fun getWeatherData(url:String, map:Boolean){
        val client=OkHttpClient()
        val request=okhttp3.Request.Builder().url(url).build()
        client.newCall(request).enqueue(object:okhttp3.Callback{
            override fun onResponse(call: Call?, response: Response?) {
                val result=response?.body()?.string()
                    val gson= Gson()
                    if (map){
                        val responseGson = gson.fromJson(result, com.example.iram.weatherapp.Gson.ResultMap::class.java)
                        this@WeatherActivity.runOnUiThread {

                            if(responseGson.list!=null){
                                val builder= AlertDialog.Builder(context)
                                builder.setTitle("Select location")
                                val adaptadorDialogo= ArrayAdapter<String>(context, android.R.layout.simple_selectable_list_item)
                                for (nameLocation in responseGson.list!!){
                                    adaptadorDialogo.add(nameLocation.name)
                                }
                                builder.setAdapter(adaptadorDialogo){
                                    dialog, which->
                                    val urlWeather:String=if (metric) "http://api.openweathermap.org/data/2.5/weather?q=${responseGson.list?.get(which)!!.name}&appid=00be396ea806be96732f1beffcf2f828&units=$unit"
                                    else "http://api.openweathermap.org/data/2.5/weather?q=${responseGson.list?.get(which)!!.name}&appid=00be396ea806be96732f1beffcf2f828"
                                    getWeatherData(urlWeather, false)
                                }
                                builder.setNegativeButton("Cancel"){
                                    dialog, which->
                                    dialog.dismiss()
                                }
                                builder.setCancelable(false)
                                builder.show()
                                Log.d("HTTPRESULT",result)
                            }else{
                                Toast.makeText(applicationContext, "Weather no available", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        val responseGson = gson.fromJson(result, com.example.iram.weatherapp.Gson.Result::class.java)
                        this@WeatherActivity.runOnUiThread {
                            tvCity?.text=responseGson.name
                            if (metric){
                                tvTemperature?.text="Temperature: ${responseGson.main?.temp}°C"
                                tvTempMax?.text="Temp max: ${responseGson.main?.temp_max}°C"
                                tvTempMin?.text="Temp min: ${responseGson.main?.temp_min}°C"
                            }else{
                                tvTemperature?.text="Temperature:  ${responseGson.main?.temp}°F"
                                tvTempMax?.text="Temp max: ${responseGson.main?.temp_max}°F"
                                tvTempMin?.text="Temp min: ${responseGson.main?.temp_min}°F"
                            }

                            tvStatus?.text=responseGson.weather?.get(0)!!.main
                            tvDescription?.text="Description: ${responseGson.weather?.get(0)!!.description}"

                            var urlImg:String="http://openweathermap.org/img/w/${responseGson.weather?.get(0)!!.icon}.png"
                            Log.d("IMAGE", urlImg)
                            Glide.with(this@WeatherActivity).load(urlImg).into(ivStatus)
                        }
                    }

            }
            override fun onFailure(call: Call?, e: IOException?) {
                Toast.makeText(applicationContext,"Error in request", Toast.LENGTH_SHORT).show()
            }
        })
    }
}