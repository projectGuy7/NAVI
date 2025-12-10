package com.example.navi

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.example.navi.databinding.ActivityMapsBinding
import com.example.navi.domain.navi.VolunteerLocation
import com.example.navi.presentation.DisabledLocationService
import com.example.navi.presentation.DisabledLocationService.Companion.ACTION_START
import com.example.navi.presentation.DisabledLocationService.Companion.ACTION_STOP
import com.example.navi.presentation.locationController.LocationController
import com.example.navi.presentation.viewmodels.disabled.disabledMapViewModel.DisabledMapAction
import com.example.navi.presentation.viewmodels.disabled.disabledMapViewModel.DisabledMapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val disabledMapViewModel: DisabledMapViewModel by viewModels<DisabledMapViewModel>()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var meMarker: Marker? = null

    private lateinit var radiusSpinner: Spinner
    private lateinit var actionButton: Button

    private var radius: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val radiusOptions = resources.getStringArray(R.array.radius_options)

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item_custom,      // Use your custom layout for the selected item
            radiusOptions
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom)

        // 2. Initialize the new UI elements from the binding object
        radiusSpinner = binding.radiusSpinner
        radiusSpinner.adapter = adapter
        actionButton = binding.actionButton

        radiusSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val radiusText = parent?.getItemAtPosition(position).toString()
                radius = radiusText.substringBefore(" ").toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        actionButton.setOnClickListener {
            disabledMapViewModel.onEvent(DisabledMapAction.FindVolunteersInRadius(radius))
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.getMapAsync(this)
    }

//    override fun onStop() {
//        super.onStop()
//        Intent(applicationContext, DisabledLocationService::class.java).apply {
//            action = ACTION_STOP
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(this)
//            } else {
//                startService(this)
//            }
//        }
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val myLocation = LocationController.location.value
        val myLatLng = LatLng(myLocation.latitude, myLocation.longitude)
        meMarker = mMap.addMarker(
            MarkerOptions()
                .position(myLatLng)
        )

        lifecycleScope.launch {
            LocationController.location.collect { location ->
                val myLatLng = LatLng(location.latitude, location.longitude)
                meMarker?.position = myLatLng
                mMap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng))
            }
        }
        lifecycleScope.launch {
            disabledMapViewModel.disabledStateFlow.collect { state ->
                for(volunteer: VolunteerLocation in state.volunteersLocations) {
                    val volunteerLatLng = LatLng(volunteer.latitude, volunteer.longitude)

                }
            }
        }

    }
}