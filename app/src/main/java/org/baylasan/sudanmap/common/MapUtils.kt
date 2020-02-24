package org.baylasan.sudanmap.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

enum class Zoom(zoom: Int) {
    World(1),
    Continent(5),
    City(10),
    Street(15),
    Buildings(20)
}

fun GoogleMap.customize(context: Context) {
    setMaxZoomPreference(18f)
    setMinZoomPreference(12f)
    isMyLocationEnabled = context.canEnableLocationButton()
    uiSettings.isMyLocationButtonEnabled = context.canEnableLocationButton()
    uiSettings.isMapToolbarEnabled = false

}
fun UiSettings.disableAll(){
    isZoomControlsEnabled=false
    isMapToolbarEnabled=false
    isMyLocationButtonEnabled=false
    isCompassEnabled=false
    isIndoorLevelPickerEnabled=false
    isRotateGesturesEnabled=false
    isScrollGesturesEnabled=false
    isScrollGesturesEnabledDuringRotateOrZoom=false
    isTiltGesturesEnabled=false
    isZoomGesturesEnabled=false

}

fun GoogleMap?.zoomToMyLocation(lat: Double, lng: Double, animate: Boolean = true) {

    val cameraPosition = CameraPosition.Builder()
        .target(LatLng(lat, lng))
        .zoom(12f)
        .build()
    val newCameraPosition = CameraUpdateFactory.newCameraPosition(cameraPosition)
    if (animate) {
        this?.animateCamera(newCameraPosition)
    } else
        this?.moveCamera(newCameraPosition)
}

fun Context.canEnableLocationButton(): Boolean = ContextCompat.checkSelfPermission(
    this,
    Manifest.permission.ACCESS_FINE_LOCATION
) == PackageManager.PERMISSION_GRANTED