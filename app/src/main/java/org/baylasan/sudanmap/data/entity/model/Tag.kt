package org.baylasan.sudanmap.data.entity.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Tag(
    @SerializedName("id") val id: Int,
    @SerializedName("label") val label: String
)