package com.example.deber02

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtiene el SupportMapFragment y notifica cuando el mapa esté listo
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Obtiene latitud y longitud de la intención
        val latitud = intent.getDoubleExtra("LATITUD", 0.0)
        val longitud = intent.getDoubleExtra("LONGITUD", 0.0)

        // Crea una ubicación LatLng
        val ubicacion = LatLng(latitud, longitud)

        // Mueve la cámara a la ubicación y añade un marcador
        mMap.addMarker(MarkerOptions().position(ubicacion).title("Ubicación de la Tienda"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))
    }
}