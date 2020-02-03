package org.baylasan.sudanmap.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_location_picker.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.*
import org.baylasan.sudanmap.domain.LocationViewModel
import org.baylasan.sudanmap.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationPickerActivity : AppCompatActivity(), GpsChecker.OnGpsListener {
    private val locationViewModel by viewModel<LocationViewModel>()
    private lateinit var gpsChecker: GpsChecker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)
        gpsChecker=GpsChecker(this)
        gpsChecker.turnGPSOn(this)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GpsChecker.GPS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                checkPermission()
            } else {
                gpsChecker.turnGPSOn(this)
            }
        }
    }

    override fun onGpsEnabled() {
        checkPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MainActivity.LOCATION_PERMISSION) {
            if (permissions.isNotEmpty() &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                setupMap()
            } else {
                toast("Unable to load current location, try enabling this from settings")
                setupMap()

            }
        }
    }

    private fun checkPermission() {
        if (doseNotHaveLocationPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MainActivity.LOCATION_PERMISSION
            )

        } else {
            setupMap()

        }
    }

    private fun setupMap() {
        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync { googleMap ->
            googleMap.customize(this)
            googleMap.uiSettings.isZoomControlsEnabled = true
            locationViewModel.getLocationUpdates().observe(this, Observer {
                googleMap.zoomToMyLocation(it.latitude, it.longitude, false)
            })
            googleMap.setPadding(16, 60, 16, 160)

            googleMap.setOnCameraIdleListener {
                pickLocationButton.enable()
            }
            val marker =
                googleMap.addMarker(
                    MarkerOptions().position(googleMap.cameraPosition.target).title(
                        getString(R.string.company_location_marker_title)
                    )
                )
            marker.showInfoWindow()
            googleMap.setOnCameraMoveListener {
                pickLocationButton.disable()
                marker.position = googleMap.cameraPosition.target

            }

            pickLocationButton.setOnClickListener {

                val selectedLocation = googleMap.cameraPosition.target
                setResult(
                    Activity.RESULT_OK, Intent().putExtra("picked_location", selectedLocation)
                )
                finish()
            }
        }
    }
}
