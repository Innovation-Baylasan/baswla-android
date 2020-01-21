package org.baylasan.sudanmap.data.entity.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("long") val long: Double
) : Parcelable {
    constructor() : this(0.0, 0.0)
}


