package com.example.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.maps.databinding.ActivityMapBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapsFrag) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestLocationPermission()
        setupLocationCallback()

        binding.btnConfirmarUbicacion.setOnClickListener {
            val cameraPosition = googleMap.cameraPosition.target
            val latitude = cameraPosition.latitude
            val longitude = cameraPosition.longitude
            val address = obtenerDireccion(latitude, longitude)

            val resultIntent = Intent().apply {
                putExtra("ADDRESS", address)
                putExtra("LATITUDE", latitude.toString())
                putExtra("LONGITUDE", longitude.toString())
            }
            setResult(AppCompatActivity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    googleMap.isMyLocationEnabled = true
                }
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng).title("Ubicación seleccionada"))
            obtenerDireccion(latLng.latitude, latLng.longitude)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }

        // Establecer la ubicación predeterminada en Santa Cruz, Bolivia
        val santaCruzLatLng = LatLng(-17.783327, -63.182140) // Coordenadas del centro de Santa Cruz, Bolivia
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(santaCruzLatLng, 15f))

        // Habilitar el botón de ubicación
        googleMap.setOnMyLocationButtonClickListener {
            startLocationUpdates()
            true
        }
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(currentLatLng).title("Tu ubicación"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    obtenerDireccion(location.latitude, location.longitude)
                }
            }
        }
    }

    private fun obtenerDireccion(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this)
        val addressList: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        val address = addressList?.get(0)?.getAddressLine(0)
        Toast.makeText(this, "Dirección: $address\nLat: $latitude, Lng: $longitude", Toast.LENGTH_SHORT).show()
        return if (!addressList.isNullOrEmpty()) {
            addressList[0].getAddressLine(0)
        } else {
            "Dirección no encontrada"
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
        } else {
            requestLocationPermission()
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
