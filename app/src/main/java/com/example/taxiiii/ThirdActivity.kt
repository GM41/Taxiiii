package com.example.taxiiii

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.geometry.Point
import java.util.Locale

class ThirdActivity: AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.three_activity)

        val name: TextView = findViewById(R.id.textView4)
        val number: TextView = findViewById(R.id.textView5)
        val route: TextView = findViewById(R.id.textView6)
        val changeRoute: Button = findViewById(R.id.button2)
        val callTaxi: Button = findViewById(R.id.button3)

        val latitude = intent.getDoubleExtra("my_latitude", 0.0)
        val longitude = intent.getDoubleExtra("my_longitude", 0.0)
        val userLocation = Point(latitude, longitude)

        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val address = addresses?.get(0)?.getAddressLine(0)


        route.text = address

        //System.out.println(userLocation.latitude)
        //println(userLocation.longitude)


    }
}