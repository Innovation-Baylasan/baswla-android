package org.baylasan.sudanmap.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

fun GoogleMap?.zoomToMyLocation(lat: Double, lng: Double) {
    val cameraPosition = CameraPosition.Builder()
        .target(LatLng(lat, lng))
        .zoom(12f)
        .build()
    this?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

fun Context.canEnableLocationButton(): Boolean = ContextCompat.checkSelfPermission(
    this,
    Manifest.permission.ACCESS_FINE_LOCATION
) == PackageManager.PERMISSION_GRANTED