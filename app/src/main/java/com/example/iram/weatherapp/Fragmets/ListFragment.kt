package com.example.iram.weatherapp.Fragmets


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import com.example.iram.weatherapp.Activities.WeatherActivity
import com.example.iram.weatherapp.Adapters.City
import com.example.iram.weatherapp.Adapters.CityAdapter
import com.example.iram.weatherapp.Network
import com.example.iram.weatherapp.R


class ListFragment : Fragment() {
    var listCities:ArrayList<City>?=null

    var listItems:RecyclerView?=null
    var layoutManager:RecyclerView.LayoutManager?=null
    companion object {
        var view0:View?=null
        var citiesAdapter:CityAdapter?=null
        var switchF:Switch?=null
        fun receiveData(query:String, submit:Boolean){
            if (submit){
                goWeatherResult(query, view0?.context!!)
            }else{
                citiesAdapter?.filter(query)
            }

        }
        fun goWeatherResult(cityName:String, context: Context){
            if(Network.verifyAvailableNetwork(context)){
                val intent= Intent(context, WeatherActivity::class.java)
                intent.putExtra("CITY", cityName)
                if (!com.example.iram.weatherapp.Fragmets.ListFragment.switchF?.isChecked!!)
                    intent.putExtra("UNIT", "metric")
                context.startActivity(intent)
            }else{
                Toast.makeText(context, "Internet connection no available", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view0=inflater.inflate(R.layout.fragment_list, container, false)
        switchF=view0?.findViewById(R.id.switchF)
        loadData()
        configureRecyclerView()

        return view0
    }
    private fun configureRecyclerView(){
        listItems=view0?.findViewById(R.id.listCities)
        listItems?.setHasFixedSize(true)
        layoutManager= LinearLayoutManager(view0?.context)
        listItems?.layoutManager=layoutManager
        citiesAdapter=CityAdapter(view0?.context!!, listCities!!)
        listItems?.adapter=citiesAdapter
    }
    private fun loadData(){
        listCities= ArrayList()
        listCities?.add(City("Durango, MX", "México"))
        listCities?.add(City("Mexico City, MX", "México"))
        listCities?.add(City("Saltillo, MX", "México"))
        listCities?.add(City("Guadalajara, MX", "México"))
        listCities?.add(City("Chihuahua, MX", "México"))
        listCities?.add(City("Torreón, MX", "México"))
    }


}
