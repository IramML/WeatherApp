package com.example.iram.weatherapp.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.example.iram.weatherapp.R

class ChooseActivity : AppCompatActivity() {
    var toolbar:Toolbar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        toolbar=findViewById(R.id.actionBarChoose)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
    }
}
