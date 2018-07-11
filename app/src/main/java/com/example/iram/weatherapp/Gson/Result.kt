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
class Main(temp:String, temp_min:String, temp_max: String){
    var temp:String?=null
    var temp_min:String?=null
    var temp_max:String?=null
    init {
        this.temp=temp
        this.temp_max=temp_max
        this.temp_min=temp_min
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