package com.example.tweeto

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {
   // lateinit var btnFindLoc: Button
  //  lateinit var tvLoc: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // btnFindLoc = findViewById<Button>(R.id.btnFindLoc);
       // tvLoc = findViewById<TextView>(R.id.tvLoc);

        btnFindLoc.setOnClickListener {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),1000);
            }else{
                var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
                var location : Location
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                try {
                    var city: String = hereLocation(location.latitude, location.longitude)
                    tvLoc.setText(city)
                }catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(this,"Not Found",Toast.LENGTH_LONG);

                }
            }

        }

    }



    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when{
            requestCode == 1000 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
                var location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) as Location;
                try {
                    var city: String = hereLocation(location.latitude, location.longitude)
                    tvLoc.text = city
                }catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(this,"Not Found",Toast.LENGTH_LONG);

                }

            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG);

            }


        }
    }

    fun hereLocation(lat:Double,lon:Double):String{
       var cityName = ""
        var geocoder = Geocoder(this,Locale.getDefault())
        var addresses : List<Address>
        try {
            addresses = geocoder.getFromLocation(lat,lon,10);
            if (addresses.size > 0){
                for (adr in addresses){
                    if (adr.locality != null && adr.getLocality().isNotEmpty()){
                        cityName = adr.locality;
                        break;

                    }

                }

            }
        }catch (e:IOException){
            e.printStackTrace();
        }
        return cityName;

    }
}
