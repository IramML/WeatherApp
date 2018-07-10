package com.example.iram.weatherapp.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.iram.weatherapp.Network
import com.example.iram.weatherapp.R

class ChooseActivity : AppCompatActivity() {
    var toolbar:Toolbar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        toolbar=findViewById(R.id.actionBarChoose)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_choose, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.icMap->{
                if(Network.verifyAvailableNetwork(this)){
                    val intent =Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Internet connection no available", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            else ->return super.onOptionsItemSelected(item)

        }

    }
}
