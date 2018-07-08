package com.example.iram.weatherapp.Gson

class Result(weather:ArrayList<Weather>, main:Main,  name:String){
    var name:String?=null
    var weather:ArrayList<Weather>?=null
    var main:Main?=null
    init {
        this.name=name
        this.weather=weather
        this.main=main
    }
}
