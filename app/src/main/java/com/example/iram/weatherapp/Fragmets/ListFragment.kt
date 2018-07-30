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
import com.example.iram.weatherapp.Activities.ChooseActivity
import com.example.iram.weatherapp.Activities.WeatherActivity
import com.example.iram.weatherapp.Adapters.City
import com.example.iram.weatherapp.Adapters.CityAdapter
import com.example.iram.weatherapp.Interfaces.ClickListener
import com.example.iram.weatherapp.R


class ListFragment : Fragment() {
    var listCities:ArrayList<City>?=null

    var listItems:RecyclerView?=null
    var layoutManager:RecyclerView.LayoutManager?=null
    companion object {
        var view0:View?=null
        var citiesAdapter:CityAdapter?=null
        fun receiveData(query:String, submit:Boolean){
            if (submit){
                goWeatherResult(query, view0?.context!!)
            }else{
                citiesAdapter?.filter(query)
            }

        }
        fun goWeatherResult(cityName:String, context: Context){
                val intent= Intent(context, WeatherActivity::class.java)
                intent.putExtra("CITY", cityName)
                context.startActivity(intent)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view0=inflater.inflate(R.layout.fragment_list, container, false)
        loadData()
        configureRecyclerView()

        return view0
    }
    private fun configureRecyclerView(){
        listItems=view0?.findViewById(R.id.listCities)
        listItems?.setHasFixedSize(true)
        layoutManager= LinearLayoutManager(view0?.context)
        listItems?.layoutManager=layoutManager
        citiesAdapter=CityAdapter(view0?.context!!, listCities!!, object:ClickListener{
            override fun onClick(view: View, index: Int) {
                   goWeatherResult(listCities?.get(index)?.nameCity!!, view0!!.context)
            }
        })
        listItems?.adapter=citiesAdapter
    }
    private fun loadData(){
        listCities= ArrayList()
        val res = resources
        val mexicoCities = res.getStringArray(R.array.nameCitiesMexico)
        for (mxCities in mexicoCities){
            listCities?.add(City(mxCities,"México"))
        }
    }


}
