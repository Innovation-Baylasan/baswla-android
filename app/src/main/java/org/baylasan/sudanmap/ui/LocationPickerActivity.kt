package org.baylasan.sudanmap.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_location_picker.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.common.customize
import org.baylasan.sudanmap.common.disable
import org.baylasan.sudanmap.common.enable
import org.baylasan.sudanmap.common.zoomToMyLocation
import org.baylasan.sudanmap.domain.LocationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationPickerActivity : AppCompatActivity() {
    private val locationViewModel by viewModel<LocationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)

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
