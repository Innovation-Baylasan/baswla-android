package org.baylasan.sudanmap.domain.entity.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Location(
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("long")
    val long: Double = 0.0
)