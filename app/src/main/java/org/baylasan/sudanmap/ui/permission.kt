package org.baylasan.sudanmap.ui


import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.location.LocationManagerCompat


class PermissionPreference(context: Context) {

    private val sharedPreferences: SharedPreferences
    private val MY_PREF = "permission"

    init {
        sharedPreferences = context.getSharedPreferences(MY_PREF, MODE_PRIVATE)
    }

    fun firstTimeAsking(permission: String, isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean(permission, isFirstTime).apply()
    }

    fun isFirstTimeAsking(permission: String): Boolean {
        return sharedPreferences.getBoolean(permission, true)
    }


}


class PermissionManager(private val context: Context) {
    private val preference: PermissionPreference =
        PermissionPreference(context)

    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun shouldAskPermission(permission: String): Boolean {
        if (shouldAskPermission()) {
            val permissionResult = ActivityCompat.checkSelfPermission(context, permission)
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    private fun isGpsEnabled(): Boolean {
        return LocationManagerCompat.isLocationEnabled(context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission(permission: String, listener: PermissionAskListener) {
        if (isGpsEnabled()) {
            if (shouldAskPermission(permission)) {
                //shoul
                if (shouldShowRequestPermissionRationale(context as AppCompatActivity, permission)) {

                    listener.onPermissionPreviouslyDenied()
                } else {

                    if (preference.isFirstTimeAsking(permission)) {
                        preference.firstTimeAsking(permission, false)

                        listener.onNeedPermission()
                    } else {

                        listener.onPermissionPreviouslyDeniedWithNeverAskAgain()
                    }
                }
            } else {

                listener.onPermissionGranted()
            }
        } else {

            listener.onGpsDisabled()
        }
    }

    interface PermissionAskListener {
        fun onNeedPermission()
        fun onPermissionPreviouslyDenied()
        fun onPermissionPreviouslyDeniedWithNeverAskAgain()
        fun onPermissionGranted()
        fun onGpsDisabled()
    }
}