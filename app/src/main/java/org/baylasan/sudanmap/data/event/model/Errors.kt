package org.baylasan.sudanmap.data.event.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Errors(
    @SerializedName("end_date") val endDate: List<String>,
    @SerializedName("latitude") val latitude: List<String>,
    @SerializedName("link") val link: List<String>,
    @SerializedName("longitude") val longitude: List<String>,
    @SerializedName("name") val name: List<String>,
    @SerializedName("start_date") val startDate: List<String>
)