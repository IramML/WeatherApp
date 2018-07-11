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
            var unit:String?=intent.getStringExtra("UNIT")
            var url: String = "http://api.openweathermap.org/data/2.5/weather?q=$nameCity&appid=00be396ea806be96732f1beffcf2f828"
            if (unit!=null){
                url+="&units=$unit"
                metric=true
            }
            getWeatherData(url, false)
        }else{
            var unit:String?=intent.getStringExtra("UNIT_MAP")
            lon=intent.getStringExtra("LON")
            lat=intent.getStringExtra("LAT")
            var url:String="http://api.openweathermap.org/data/2.5/find?lat=$lat&lon=$lon&appid=00be396ea806be96732f1beffcf2f828"
            if (unit!=null){
                url+="&units=$unit"
                metric=true
            }
            getWeatherData(url, true)
        }
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
                                    /*fotoIndex=which
                                    foto?.setImageResource(obtenerFoto(fotoIndex))*/
                                    tvCity?.text=responseGson.list?.get(which)!!.name
                                    if (metric){
                                        tvTemperature?.text="${responseGson.list?.get(which)!!.main?.temp}°C"
                                    }else{
                                        tvTemperature?.text="${responseGson.list?.get(which)!!.main?.temp}°F"
                                    }
                                    tvStatus?.text=responseGson.list?.get(which)!!.weather?.get(0)!!.main
                                    tvDescription?.text=responseGson.list?.get(which)!!.weather?.get(0)!!.description

                                    var urlImg:String="http://openweathermap.org/img/w/${responseGson.list?.get(which)!!.weather?.get(0)!!.icon}.png"
                                    Log.d("IMAGE", urlImg)
                                    Glide.with(this@WeatherActivity).load(urlImg).into(ivStatus)
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

            }
            override fun onFailure(call: Call?, e: IOException?) {
                Toast.makeText(applicationContext,"Error in request", Toast.LENGTH_SHORT).show()
            }
        })
    }
}