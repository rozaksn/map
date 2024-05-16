package com.example.map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.map.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
       ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var mMaps:GoogleMap
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val  mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MainActivity)
        database = FirebaseDatabase.getInstance().reference

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMaps = googleMap
        fetchLocationData()

    }

    private fun fetchLocationData(){
        database.child("location").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (locationSnapshot in snapshot.children) {
                    val latitude = locationSnapshot.child("lat").getValue().toString().toDouble()
                    val longitude = locationSnapshot.child("lon").getValue().toString().toDouble()

                    if (latitude != null && longitude != null) {
                        val location = LatLng(latitude, longitude)
                        mMaps.addMarker(MarkerOptions().position(location).title("Marker").snippet("Latitude: $latitude, Longitude: $longitude"))
                        mMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}