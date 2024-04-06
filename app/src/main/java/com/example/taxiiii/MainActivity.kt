package com.example.taxiiii

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager
    private var placemark: PlacemarkMapObject? = null
    private lateinit var newPoint: Point
    private lateinit var userLocation: Point

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("435b8ab4-1d23-4ec6-af60-55032d49f3f4")
        MapKitFactory.initialize(this@MainActivity)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapview)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("number")


        findViewById<Button>(R.id.my_button).setOnClickListener {
            checkLocationPermissionAndGetLocation()
        }

        mapView.map.addInputListener(object : InputListener {
            override fun onMapTap(map: Map, point: Point) {
                placemark?.let {
                    map.mapObjects.remove(it)
                }

                placemark = map.mapObjects.addPlacemark(point).apply {
                    opacity = 0.5f
                    setIcon(ImageProvider.fromResource(this@MainActivity, R.drawable.img))
                }
                newPoint = Point(point.latitude, point.longitude)
            }

            override fun onMapLongTap(p0: com.yandex.mapkit.map.Map, p1: Point){
            }
        })

        findViewById<Button>(R.id.button4).setOnClickListener {
            if(!::userLocation.isInitialized || newPoint == null) {
                Toast.makeText(this@MainActivity, "Нужны две точки для создания маршрута", Toast.LENGTH_LONG).show()
            }else{
                val intent = Intent(this, ThirdActivity::class.java).apply {
                    putExtra("name", name)
                    putExtra("number", phone)
                    putExtra("my_latitude", userLocation.latitude)
                    putExtra("my_longitude", userLocation.longitude)
                    putExtra("new_point_latitude", newPoint.latitude)
                    putExtra("new_point_longitude", newPoint.longitude)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun checkLocationPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        } else {
            getLocationAndUpdateMap()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationAndUpdateMap()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getLocationAndUpdateMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        userLocation = Point(location.latitude, location.longitude)
        mapView.map.move(CameraPosition(userLocation, 14.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH, 1f), null)
        mapView.map.mapObjects.addPlacemark(userLocation).apply {
            setIcon(ImageProvider.fromResource(this@MainActivity, R.drawable.ic_pin))
        }
        locationManager.removeUpdates(this)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onRestart() {
        super.onRestart()
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}
