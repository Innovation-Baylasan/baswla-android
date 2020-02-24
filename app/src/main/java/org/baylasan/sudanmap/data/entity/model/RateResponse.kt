package org.baylasan.sudanmap.data.entity.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RateResponse(
    @SerializedName("avg_rate") val avgRate: String,
    @SerializedName("message") val message: String)