package com.example.frontend.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.frontend.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class LocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var searchButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        searchButton = view.findViewById(R.id.search_button)

        searchButton.setOnClickListener {
            // Implement your search functionality here
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val klagenfurt = LatLng(46.6249, 14.3050)
        mMap.addMarker(MarkerOptions().position(klagenfurt).title("Marker in Klagenfurt"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(klagenfurt, 15f))
    }
}
