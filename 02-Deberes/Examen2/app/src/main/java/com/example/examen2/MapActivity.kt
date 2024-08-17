package com.example.examen2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latitud = intent.getDoubleExtra("LATITUD", 0.0)
        val longitud = intent.getDoubleExtra("LONGITUD", 0.0)
        val location = LatLng(latitud, longitud)
        googleMap.addMarker(MarkerOptions().position(location).title("Ubicación de la tienda"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}