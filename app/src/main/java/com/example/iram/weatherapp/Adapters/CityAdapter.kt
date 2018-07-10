package com.example.iram.weatherapp.Adapters

import android.content.Context
import android.content.Intent
import android.support.v4.app.ListFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.iram.weatherapp.Activities.ChooseActivity
import com.example.iram.weatherapp.Activities.WeatherActivity
import com.example.iram.weatherapp.Interfaces.Search
import com.example.iram.weatherapp.Interfaces.Send
import com.example.iram.weatherapp.Network
import com.example.iram.weatherapp.R

class CityAdapter(var context: Context, items:ArrayList<City>): RecyclerView.Adapter<CityAdapter.ViewHolder>(), Send{
    override fun sendCityName(cityName:String) {
        com.example.iram.weatherapp.Fragmets.ListFragment.goWeatherResult(cityName, context)
    }
    var items:ArrayList<City>?=null
    var copyItem:ArrayList<City>?=null
    init {
        this.items=items
        this.copyItem=items
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.template, p0, false)
        val viewHolder=ViewHolder(view)
        return viewHolder
    }
    fun filter(query:String){
        if (query==null || query==""){
            items=ArrayList(copyItem)
            notifyDataSetChanged()
            return
        }else{
            items?.clear()
            var search=query
            search=search.toLowerCase()
            for (item in copyItem!!){
                val nombre=item.nameCity?.toLowerCase()
                if (nombre!!.contains(search)){
                    items?.add(item)
                }
            }
            notifyDataSetChanged()
        }

    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvCity?.text=items?.get(position)?.nameCity
        viewHolder.tvCountry?.text=items?.get(position)?.nameCountry
        viewHolder.layout?.setOnClickListener {
            sendCityName(items?.get(position)?.nameCity!!)
        }
    }
    override fun getItemCount(): Int {
        return items!!.count()
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var tvCity:TextView?=null
        var tvCountry:TextView?=null
        var layout:LinearLayout?=null
        init {
            tvCity=view.findViewById(R.id.tvCity)
            tvCountry=view.findViewById(R.id.tvCountry)
            layout=view.findViewById(R.id.LayoutContent)
        }
    }
}