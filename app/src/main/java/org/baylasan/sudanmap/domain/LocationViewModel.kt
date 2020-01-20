package org.baylasan.sudanmap.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.baylasan.sudanmap.utils.LocationLiveData

class LocationViewModel(private val app: Application) : AndroidViewModel(app) {
    private val locationLiveDouble = LocationLiveData(app.applicationContext)

    fun getLocationUpdates(): LocationLiveData {
        return locationLiveDouble
    }
}