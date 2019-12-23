package org.baylasan.sudanmap.domain.entity.model


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Location(
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("long")
    val long: Double = 0.0
):Parcelable