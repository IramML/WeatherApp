package com.example.iram.weatherapp.Fragmets


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iram.weatherapp.Adapters.City
import com.example.iram.weatherapp.Adapters.CityAdapter
import com.example.iram.weatherapp.R


class ListFragment : Fragment() {
    var view0:View?=null
    var listCities:ArrayList<City>?=null
    var citiesAdapter:CityAdapter?=null
    var listItems:RecyclerView?=null
    var layoutManager:RecyclerView.LayoutManager?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view0=inflater.inflate(R.layout.fragment_list, container, false)
        listItems=view0?.findViewById(R.id.listCities)
        listItems?.setHasFixedSize(true)
        layoutManager= LinearLayoutManager(view0?.context)
        listItems?.layoutManager=layoutManager
        loadData()
        citiesAdapter=CityAdapter(view0?.context!!, listCities!!)
        listItems?.adapter=citiesAdapter
        return view0
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
