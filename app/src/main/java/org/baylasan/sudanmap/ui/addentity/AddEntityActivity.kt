package org.baylasan.sudanmap.ui.addentity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_add_entity.*
import kotlinx.android.synthetic.main.content_add_entity.*
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.ui.view.AppBarChangedListener
import org.baylasan.sudanmap.common.GpsChecker
import org.baylasan.sudanmap.common.LocationLiveData
import org.baylasan.sudanmap.common.canEnableLocationButton
import org.baylasan.sudanmap.common.toLatLng

class AddEntityActivity : AppCompatActivity(), GpsChecker.OnGpsListener {
    private lateinit var gpsChecker: GpsChecker
    private val onStateChanged = object : AppBarChangedListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            if (state == State.COLLAPSED) {
                toolbar.setBackgroundColor(
                    ContextCompat.getColor(
                        this@AddEntityActivity,
                        R.color.yellowAccent
                    )
                )
            } else {
                toolbar.setBackgroundColor(
                    ContextCompat.getColor(
                        this@AddEntityActivity,
                        android.R.color.transparent
                    )
                )
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entity)
        mapView.onCreate(savedInstanceState)
        gpsChecker = GpsChecker(this)
        gpsChecker.turnGPSOn(this)

        appBar.addOnOffsetChangedListener(onStateChanged)


    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    private fun setupMapAndLocation() {
        mapView.getMapAsync { googleMap ->
            googleMap.uiSettings.isMyLocationButtonEnabled = canEnableLocationButton()
            googleMap.isMyLocationEnabled = canEnableLocationButton()

            val marker =
                googleMap.addMarker(MarkerOptions().position(googleMap.cameraPosition.target))
            googleMap.setOnCameraMoveListener {
                marker.position = googleMap.cameraPosition.target
            }

            LocationLiveData(this).observe(this, Observer { location ->
                Log.d("MEGA", "Location: $location")
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location.toLatLng(), 12f))


            })
        }
    }

    override fun onStop() {
        super.onStop()
        appBar.removeOnOffsetChangedListener(onStateChanged)
        mapView.onStop()
    }


    override fun onGpsEnabled() {
        setupMapAndLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GpsChecker.GPS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                setupMapAndLocation()
            } else {
                gpsChecker.turnGPSOn(this)
            }
        }
    }


}
