package com.example.iram.weatherapp.Gson

class ResultMap(list:ArrayList<List>){
    var list:ArrayList<List>?=null
    init {
        this.list=list
    }
}
class List(name:String, main: Main, weather:ArrayList<Weather>){
    var name:String?=null
    var main:Main?=null
    var weather:ArrayList<Weather>?=null
    init {
        this.name=name
        this.main=main
        this.weather=weather
    }
}
