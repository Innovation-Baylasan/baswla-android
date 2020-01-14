package org.baylasan.sudanmap.data.user.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Role(
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("role")
    val role: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)