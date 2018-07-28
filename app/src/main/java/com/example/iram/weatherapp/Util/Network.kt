package com.example.iram.weatherapp.Util

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.iram.weatherapp.Interfaces.HttpResponse
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

class Network(var activity: AppCompatActivity){
    fun verifyAvailableNetwork():Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
    fun httpRequest(context: Context, url:String, httpResponse:HttpResponse){
        Log.d("URL_REQUEST", url)
        if(verifyAvailableNetwork()) {
            val client= OkHttpClient()
            val request=okhttp3.Request.Builder().url(url).build()
            client.newCall(request).enqueue(object:okhttp3.Callback{
                override fun onResponse(call: Call?, response: Response?) {
                    Log.d("HTTP_REQUEST", response?.body().toString())
                    httpResponse.httpResponseSuccess(response?.body()?.string()!!)
                }
                override fun onFailure(call: Call?, e: IOException?) {
                    Toast.makeText(activity.applicationContext,"Error in HTTP request", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Toast.makeText(activity.applicationContext, "No network available", Toast.LENGTH_SHORT).show()
        }
    }
}