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
import com.example.iram.weatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var toolbar: Toolbar?=null
    var switchF:Switch?=null
    //current location
    var callback: LocationCallback?=null
    private val permissionFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permissionCorseLocation= android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val CODE_LOCATION_PERMISSION=100
    var locationRequest: LocationRequest?=null

    var fusedLocationClient: FusedLocationProviderClient?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        toolbar = findViewById(R.id.actionBarMap)
        toolbar?.setTitle(R.string.app_name)

        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        switchF=findViewById(R.id.switchFMap)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = FusedLocationProviderClient(this)
        inicializeLocationRequest()

        callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (mMap != null) {
                    mMap.isMyLocationEnabled = true
                    mMap.uiSettings.isMyLocationButtonEnabled = true
                }
            }
        }
    }
    private fun prepareMarkers(){
        mMap.setOnMapLongClickListener { location: LatLng? ->
            val intent=Intent(this, WeatherActivity::class.java)
            intent.putExtra("LAT", location?.latitude.toString())
            intent.putExtra("LON", location?.longitude.toString())
            if (!switchF?.isChecked!!)
                intent.putExtra("UNIT_MAP", "metric")
            startActivity(intent)
        }

   }
    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(applicationContext,"Long click to choose location weather", Toast.LENGTH_SHORT).show()
        mMap = googleMap
        prepareMarkers()
    }
     fun inicializeLocationRequest(){
        locationRequest= LocationRequest()
        locationRequest?.interval=10000
        locationRequest?.fastestInterval=5000
        locationRequest?.priority=LocationRequest.PRIORITY_HIGH_ACCURACY

    }
    private fun validatePermissionsLocation():Boolean{
        val preciseLocalitation= ActivityCompat.checkSelfPermission(this, permissionFineLocation)== PackageManager.PERMISSION_GRANTED
        val ordinaryLocation=ActivityCompat.checkSelfPermission(this, permissionCorseLocation )== PackageManager.PERMISSION_GRANTED
        return preciseLocalitation && ordinaryLocation
    }
    @SuppressLint("MissingPermission")
    private fun getLocation(){
        fusedLocationClient?.requestLocationUpdates(locationRequest, callback, null)
    }
    private fun askPermission(){
        val provideContext = ActivityCompat.shouldShowRequestPermissionRationale(this, permissionFineLocation)

        if (provideContext){
            requestPermission()
        }else{
            requestPermission()
        }
    }
    private fun requestPermission(){
        requestPermissions(arrayOf(permissionFineLocation, permissionCorseLocation), CODE_LOCATION_PERMISSION)
    }
    private fun stopUpdateLocation(){
        fusedLocationClient?.removeLocationUpdates(callback)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CODE_LOCATION_PERMISSION->{
                if (grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                }else{
                    Toast.makeText(this, "You did not give permission",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()

        if(validatePermissionsLocation()){
            getLocation()
        }else{
            askPermission()
        }
    }
    override fun onPause() {
        super.onPause()
        stopUpdateLocation()
    }
}
