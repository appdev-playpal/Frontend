package com.example.frontend.fragments


import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.frontend.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest
import android.location.Geocoder
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import java.io.IOException


@Suppress("DEPRECATION")
class LocationFragment : Fragment(), OnMapReadyCallback, SensorEventListener {

    // Google Map instance to interact with the map
    private lateinit var mMap: GoogleMap
    private lateinit var searchButton: Button
    private lateinit var searchField: EditText
    // FusedLocationProviderClient for getting location updates
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // SensorManager for handling sensor data
    private lateinit var sensorManager: SensorManager
    // Arrays for storing sensor data
    private var rotationMatrix = FloatArray(9)
    private var orientation = FloatArray(3)
    // Current degree of rotation for the compass
    private var currentDegree = 0f

    // Register for location permission request (with ChatGPT)
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            // Show a toast if permission is denied
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    // Set up views and map when the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the FusedLocationProviderClient and SensorManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Get the map fragment and set up the map asynchronously
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        searchButton = view.findViewById(R.id.search_button)
        searchField = view.findViewById(R.id.search_field)

        searchButton.setOnClickListener {
            val locationName = searchField.text.toString()
            if (locationName.isNotEmpty()) {
                // Search for the location if the input is not empty
                searchLocation(locationName)
            } else {
                // Show a toast if no location is entered
                Toast.makeText(requireContext(), "Please enter a location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Search for a location by name and display it on the map (with ChatGPT)
    private fun searchLocation(locationName: String) {
        val geocoder = Geocoder(requireContext())
        try {
            // Get a list of addresses for the location name
            val addressList = geocoder.getFromLocationName(locationName, 1)
            if (addressList!!.isNotEmpty()) {
                // If addresses are found, get the first address
                val address = addressList[0]
                val latLng = LatLng(address!!.latitude, address.longitude)
                // Add a marker and move the camera to the location
                mMap.addMarker(MarkerOptions().position(latLng).title(locationName))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } else {
                // Show a toast if the location is not found
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Show a toast if there's an error with the Geocoder service
            Toast.makeText(requireContext(), "Geocoder service not available", Toast.LENGTH_SHORT).show()
        }
    }

    // Check and request location permission if needed (with ChatGPT)
    private fun checkLocationPermission() {
        when {
            // If location permission is already granted, get the current location
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }
            // Show rationale if permission is needed and should be explained to the user
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(requireContext(), "Location permission is needed to show your current location on the map", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Request location permission
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // Get the current location and update the map
    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Get the last known location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    // Add a marker at the current location and move the camera
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        }
    }

    // Callback when the map is ready to use
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Enable zoom controls on the map
        mMap.uiSettings.isZoomControlsEnabled = true
        // Enable the user's current location on the map if permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            checkLocationPermission()
        }
    }

    // Register the sensor listener when the fragment is resumed
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_UI)
    }

    // Unregister the sensor listener when the fragment is paused
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // Handle sensor data changes to update the compass rotation (with ChatGPT)
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            // Get the rotation matrix and orientation from the sensor data
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientation)
            val azimuthInRadians = orientation[0]
            val azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()
            // Create a rotation animation for the compass
            val rotateAnimation = RotateAnimation(
                currentDegree,
                -azimuthInDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
            rotateAnimation.duration = 250
            rotateAnimation.fillAfter = true
            // Update the current degree for the next animation
            currentDegree = -azimuthInDegrees
        }
    }

    // Handle changes in sensor accuracy
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }
}

