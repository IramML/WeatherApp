package com.example.iram.weatherapp.Gson

class Weather(main:String, description:String, icon:String){
    var main:String?=null
    var description:String?=null
    var icon:String?=null
    init {
        this.main=main
        this.description=description
        this.icon=icon
    }
}