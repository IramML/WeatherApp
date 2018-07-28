package com.example.iram.weatherapp.Activities


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import com.bumptech.glide.Glide
import com.example.iram.weatherapp.Interfaces.weatherByLocationInterface
import com.example.iram.weatherapp.Interfaces.weatherByNameInterface
import com.example.iram.weatherapp.OpenWeatherMap.openWeatherMap
import com.example.iram.weatherapp.OpenWeatherMap.openWeatherMapAPILocation
import com.example.iram.weatherapp.OpenWeatherMap.openWeatherMapAPIName
import com.example.iram.weatherapp.R
import android.widget.*


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
    var tvTempMin:TextView?=null
    var tvTempMax:TextView?=null

    var unit:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        initViews()
        initToolbar()
        getExtras()
        if(nameCity!=null){
            getWeatherData(nameCity!!, unit!!)
        }else{
            getWeatherByLocation(lat!!, lon!!)
        }
    }
    private fun getExtras() {
        nameCity=intent.getStringExtra("CITY")
        lon=intent.getStringExtra("LON")
        lat=intent.getStringExtra("LAT")
        unit=intent.getStringExtra("UNIT")
        if (unit!=null) unit="&units=$unit"
        else unit=""

    }

    private fun initViews() {
        ivStatus=findViewById(R.id.ivStatus)
        tvCity=findViewById(R.id.tvCity)
        tvStatus=findViewById(R.id.tvStatus)
        tvTemperature=findViewById(R.id.tvTemperature)
        tvDescription=findViewById(R.id.tvDescription)
        tvTempMax=findViewById(R.id.tvTempMax)
        tvTempMin=findViewById(R.id.tvTempMin)
    }
    private fun initToolbar() {
        toolbar=findViewById(R.id.actionBarWeather)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getWeatherData(nameCity:String, unit:String){
        val openWeatherMap=openWeatherMap(this)
        openWeatherMap.getWeatherByName(nameCity, unit, object:weatherByNameInterface{
            override fun getWeatherByName(result: openWeatherMapAPIName) {
                this@WeatherActivity.runOnUiThread {
                    tvDescription?.text=result.weather?.get(0)?.description!!
                    tvCity?.text=result.name
                    val m=if (!unit.isNullOrEmpty())"°C" else "°F"

                    tvTemperature?.text="Temperature: ${result.main?.temp}$m"
                    tvTempMax?.text="Temp max: ${result.main?.temp_max}$m"
                    tvTempMin?.text="Temp min: ${result.main?.temp_min}$m"

                    tvStatus?.text=result.weather?.get(0)!!.main
                    tvDescription?.text="Description: ${result.weather?.get(0)!!.description}"

                    var urlImg:String="http://openweathermap.org/img/w/${result.weather?.get(0)!!.icon}.png"
                    Log.d("IMAGE", urlImg)
                    Glide.with(this@WeatherActivity).load(urlImg).into(ivStatus)
                }
            }
        })
    }
    private fun getWeatherByLocation(lat:String, lon:String){
        val openWeatherMap=openWeatherMap(this)
        openWeatherMap.getWeatherByLocation(lat, lon, object:weatherByLocationInterface{
            override fun getWeatherByLocation(result: openWeatherMapAPILocation) {
                this@WeatherActivity.runOnUiThread {
                    if(result.list!=null){
                        val sMetric=android.widget.Switch(this@WeatherActivity)
                        sMetric.text="°F"

                        val builder= AlertDialog.Builder(this@WeatherActivity).setView(sMetric)
                        builder.setTitle("Select location")
                        val adaptadorDialogo= ArrayAdapter<String>(this@WeatherActivity, android.R.layout.simple_selectable_list_item)
                        for (nameLocation in result.list!!){
                            adaptadorDialogo.add(nameLocation.name)
                        }
                        builder.setAdapter(adaptadorDialogo){
                            dialog, which->
                            getWeatherData(result.list?.get(which)?.name!!,
                                    if (sMetric.isChecked)"" else "&units=metric")
                        }
                        builder.setNegativeButton("Cancel"){
                            dialog, which->
                            finish()
                            dialog.dismiss()
                        }
                        builder.setCancelable(false)
                        builder.show()
                    }else{
                        Toast.makeText(applicationContext, "Weather no available", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

        })
    }
}