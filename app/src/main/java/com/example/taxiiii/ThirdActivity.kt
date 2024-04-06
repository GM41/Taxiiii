package com.example.taxiiii

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import java.util.Locale
import kotlin.math.roundToInt

class ThirdActivity: AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.three_activity)

        val name: TextView = findViewById(R.id.textView4)
        val number: TextView = findViewById(R.id.textView5)
        val dist: TextView = findViewById(R.id.textView8)
        val route1: TextView = findViewById(R.id.textView6)
        val route2: TextView = findViewById(R.id.textView7)
        val changeRoute: Button = findViewById(R.id.button2)
        val callTaxi: Button = findViewById(R.id.button3)

        val fullName = intent.getStringExtra("name")
        val phone = intent.getStringExtra("number")
        val my_latitude = intent.getDoubleExtra("my_latitude", 0.0)
        val my_longitude = intent.getDoubleExtra("my_longitude", 0.0)
        val latitude = intent.getDoubleExtra("new_point_latitude", 1.0)
        val longitude = intent.getDoubleExtra("new_point_longitude", 1.0)
        val my_point = Point(my_latitude, my_longitude)
        val new_point = Point(latitude, longitude)
        val distance = calculateDistance(my_point, new_point);


        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(my_latitude, my_longitude, 1)
        val addresses1 = geocoder.getFromLocation(latitude, longitude, 1)
        val address = addresses?.get(0)?.getAddressLine(0)
        val address1 = addresses1?.get(0)?.getAddressLine(0)

        name.text = fullName
        number.text = phone
        dist.text = "Расстояние: " + distance.toString() + " км."
        route1.text = "Откуда: " + address
        route2.text = "Куда: " + address1

        changeRoute.setOnClickListener {
            Log.d("ThirdActivity", "Attempting to start MainActivity")
            val intent1 = Intent(this@ThirdActivity, MainActivity::class.java)
            intent1.putExtra("ApiKey", "435b8ab4-1d23-4ec6-af60-55032d49f3f4")
            startActivity(intent1)
            finish()
        }

        callTaxi.setOnClickListener {
            Toast.makeText(this@ThirdActivity, "Такси вызвано, ожидайте", Toast.LENGTH_LONG).show()
        }
    }

    fun calculateDistance(pointA: Point, pointB: Point): Double {
        val R = 6371
        val latDistance = Math.toRadians(pointB.latitude - pointA.latitude)
        val lonDistance = Math.toRadians(pointB.longitude - pointA.longitude)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(pointA.latitude)) * Math.cos(Math.toRadians(pointB.latitude)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return ((((R * c)*100).roundToInt())/100).toDouble()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }
}