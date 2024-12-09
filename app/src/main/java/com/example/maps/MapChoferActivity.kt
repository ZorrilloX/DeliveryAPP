package com.example.maps.chofer.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.maps.R
import com.example.maps.databinding.ActivityMapChoferBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapChoferActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapChoferBinding
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapChoferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener latitud y longitud pasadas por el intent
        val latitude = intent.getDoubleExtra("LATITUDE", 0.0)
        val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)

        // Configurar el fragmento de mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragmentChofer) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Mostrar la latitud y longitud en un Toast
        Toast.makeText(this, "Lat: $latitude, Long: $longitude", Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Centrar el mapa en la latitud y longitud recibidas
        val location = LatLng(intent.getDoubleExtra("LATITUDE", 0.0), intent.getDoubleExtra("LONGITUDE", 0.0))
        googleMap.addMarker(MarkerOptions().position(location).title("Ubicación del Pedido"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}
