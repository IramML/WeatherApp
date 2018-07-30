package com.example.iram.weatherapp.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import android.widget.Toast
import com.example.iram.weatherapp.Interfaces.Search
import com.example.iram.weatherapp.Interfaces.locationListener
import com.example.iram.weatherapp.R
import com.example.iram.weatherapp.Util.Location
import com.google.android.gms.location.LocationResult

class ChooseActivity : AppCompatActivity(), Search {
    var location: Location?=null
    var lat=""
    var lng=""
    var toolbar:Toolbar?=null
    override fun sendData(text: String, submit:Boolean) {
        com.example.iram.weatherapp.Fragmets.ListFragment.receiveData(text, submit)
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        toolbar=findViewById(R.id.actionBarChoose)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
        location= Location(this, object:locationListener{
            override fun locationResponse(locationResult: LocationResult) {
                lat=locationResult.lastLocation.latitude.toString()
                lng=locationResult.lastLocation.longitude.toString()
                location?.stopUpdateLocation()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_choose, menu)

        val itemSearch= menu?.findItem(R.id.icSearch)
        var viewSearch = itemSearch?.actionView as android.support.v7.widget.SearchView
        viewSearch.queryHint="Write city..."

        viewSearch.setOnQueryTextFocusChangeListener { view, b ->
            Toast.makeText(applicationContext, "Submit to request, write to filter", Toast.LENGTH_LONG).show()
        }

        viewSearch.setOnQueryTextListener(object:android.support.v7.widget.SearchView.OnQueryTextListener{

            //when the user presses enter
            override fun onQueryTextSubmit(query: String?): Boolean {
                sendData(query!!, true)
                return true
            }
            //when the text changes
            override fun onQueryTextChange(newText: String?): Boolean {
                sendData(newText!!, false)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.icMap->{
                val intent =Intent(this, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.icLocation->{
                if (!lat.isNullOrEmpty() && !lng.isNullOrEmpty()){
                    val intent=Intent(applicationContext, WeatherActivity::class.java)
                    intent.putExtra("LAT", lat)
                    intent.putExtra("LON", lng)
                    startActivity(intent)
                }else{
                    Toast.makeText(applicationContext, "Without location", Toast.LENGTH_SHORT).show()
                }

                return true
            }
            else ->return super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        location?.inicializeLocation()
    }
    override fun onPause() {
        super.onPause()
        location?.stopUpdateLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        location?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
