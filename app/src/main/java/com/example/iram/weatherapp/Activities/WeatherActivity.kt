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


class WeatherActivity : AppCompatActivity(){
    var nameCity:String?=null
    var lat:String?=null
    var lon:String?=null
    var unit:String?=null

    var toolbar:Toolbar?=null
    var ivStatus:ImageView?=null
    var tvCity:TextView?=null
    var tvTemperature:TextView?=null
    var tvStatus:TextView?=null
    var tvDescription:TextView?=null
    var tvTempMin:TextView?=null
    var tvTempMax:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        initViews()
        initToolbar()
        getExtras()

        val builder= AlertDialog.Builder(this@WeatherActivity)
        builder.setTitle("Unit")
        val adaptadorDialogo= ArrayAdapter<String>(this@WeatherActivity, android.R.layout.simple_selectable_list_item)
        builder.setPositiveButton("°C"){ dialogInterface, i ->
            unit="&units=metric"
            if(nameCity!=null){
                getWeatherData(nameCity!!, unit!!)
            }else{
                getWeatherByLocation(lat!!, lon!!)
            }
        }
        builder.setNeutralButton("°F"){ dialogInterface, i ->
            unit=""
            if(nameCity!=null){
                getWeatherData(nameCity!!, unit!!)
            }else{
                getWeatherByLocation(lat!!, lon!!)
            }
        }
        builder.setNegativeButton("Cancel"){
            dialog, which->
            finish()
            dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }
    private fun getExtras() {
        nameCity=intent.getStringExtra("CITY")
        lon=intent.getStringExtra("LON")
        lat=intent.getStringExtra("LAT")
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
            override fun getWeatherByName(nameCity: String, urlImage: String, status: String, description: String, temperature: String, tempMin: String, tempMax: String) {
                this@WeatherActivity.runOnUiThread {
                    tvDescription?.text = description
                    tvCity?.text = nameCity
                    val m = if (!unit.isNullOrEmpty()) "°C" else "°F"

                    tvTemperature?.text = "Temperature: $temperature$m"
                    tvTempMax?.text = "Temp max: $tempMax$m"
                    tvTempMin?.text = "Temp min: $tempMin$m"

                    tvStatus?.text = status
                    tvDescription?.text = "Description: $description"
                    Log.d("IMAGE", urlImage)
                    Glide.with(this@WeatherActivity).load(urlImage).into(ivStatus)
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

                        val builder= AlertDialog.Builder(this@WeatherActivity)
                        builder.setTitle("Select location")
                        val adaptadorDialogo= ArrayAdapter<String>(this@WeatherActivity, android.R.layout.simple_selectable_list_item)
                        for (nameLocation in result.list!!){
                            adaptadorDialogo.add(nameLocation.name)
                        }
                        builder.setAdapter(adaptadorDialogo){
                            dialog, which->
                            getWeatherData(result.list?.get(which)?.name!!, unit!!)
                        }
                        builder.setNegativeButton("Cancel"){
                            dialog, which->
                            finish()
                            dialog.dismiss()
                        }
                        builder.setCancelable(false)
                        builder.show()
                    }else{
                        this@WeatherActivity.runOnUiThread {
                            Toast.makeText(applicationContext, "Weather no available", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }

        })
    }
}