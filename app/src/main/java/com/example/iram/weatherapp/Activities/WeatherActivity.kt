package com.example.iram.weatherapp.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.iram.weatherapp.R

class WeatherActivity : AppCompatActivity() {
    var nameCity:String?=null
    var ivStatus:ImageView?=null
    var tvCity:TextView?=null
    var tvTemperature:TextView?=null
    var tvStatus:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        ivStatus=findViewById(R.id.ivStatus)
        tvCity=findViewById(R.id.tvCity)
        tvStatus=findViewById(R.id.tvStatus)
        tvTemperature=findViewById(R.id.tvTemperature)

        nameCity=intent.getStringExtra("CITY")
        tvCity?.text=nameCity
    }
}
