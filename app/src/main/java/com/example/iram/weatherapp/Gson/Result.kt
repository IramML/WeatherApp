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
class Main(temp:String){
    var temp:String?=null
    init {
        this.temp=temp
    }
}
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