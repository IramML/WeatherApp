package com.example.iram.weatherapp.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.widget.Switch
import android.widget.Toast
import com.example.iram.weatherapp.Interfaces.locationListener
import com.example.iram.weatherapp.R
import com.example.iram.weatherapp.Util.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    var location: Location?=null
    private lateinit var mMap: GoogleMap
    var toolbar:Toolbar?=null
    var switchF:Switch?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        toolbar = findViewById(R.id.actionBarMap)
        toolbar?.setTitle(R.string.app_name)

        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        switchF=findViewById(R.id.sMetric)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        location?.inicializeLocation()
        location= Location(this, object:locationListener{
            override fun locationResponse(locationResult: LocationResult) {
                if (mMap != null) {
                    mMap.isMyLocationEnabled = true
                    mMap.uiSettings.isMyLocationButtonEnabled = true
                }
            }

        })
    }
    private fun prepareMarkers(){
        mMap.setOnMapLongClickListener { location: LatLng? ->
            val intent=Intent(this, WeatherActivity::class.java)
            intent.putExtra("LAT", location?.latitude.toString())
            intent.putExtra("LON", location?.longitude.toString())
            startActivity(intent)
        }

   }
    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(applicationContext,"Long click to choose location weather", Toast.LENGTH_LONG).show()
        mMap = googleMap
        prepareMarkers()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        location?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onStart() {
        super.onStart()
        location?.inicializeLocation()
    }
    override fun onPause() {
        super.onPause()
        location?.stopUpdateLocation()
    }
}
