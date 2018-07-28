package com.example.iram.weatherapp.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.iram.weatherapp.Interfaces.ClickListener
import com.example.iram.weatherapp.Interfaces.Send
import com.example.iram.weatherapp.R

class CityAdapter(var context: Context, items:ArrayList<City>,var clickListener: ClickListener): RecyclerView.Adapter<CityAdapter.ViewHolder>(){
    var items:ArrayList<City>?=null
    var copyItem:ArrayList<City>?=null
    init {
        this.items=items
        this.copyItem=items
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.template, p0, false)
        val viewHolder=ViewHolder(view, clickListener)
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

    }
    override fun getItemCount(): Int {
        return items!!.count()
    }
    class ViewHolder(view: View, clickListener: ClickListener):RecyclerView.ViewHolder(view), View.OnClickListener{
        var tvCity:TextView?=null
        var tvCountry:TextView?=null
        var layout:LinearLayout?=null
        var clickListener:ClickListener?=null
        var cardView:CardView?=null
        init {
            tvCity=view.findViewById(R.id.tvCity)
            tvCountry=view.findViewById(R.id.tvCountry)
            layout=view.findViewById(R.id.LayoutContent)
            cardView=view.findViewById(R.id.cardView)
            this.clickListener=clickListener
            cardView!!.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            clickListener?.onClick(p0!!, adapterPosition)
        }
    }
}