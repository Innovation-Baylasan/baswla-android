package org.baylasan.sudanmap.domain.entity.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
    data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("long") val long: Double
    )