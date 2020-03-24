package org.baylasan.sudanmap.data.entity.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AddEntityResponseError(
    @SerializedName("errors") val fields: Fields,
    var httpCode: Int
)