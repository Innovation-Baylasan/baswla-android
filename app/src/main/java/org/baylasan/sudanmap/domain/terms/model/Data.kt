package org.baylasan.sudanmap.domain.terms.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Data(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("id") val id: Int,
    @SerializedName("key") val key: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("value") val value: String
)