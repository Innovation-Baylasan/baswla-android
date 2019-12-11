package org.baylasan.sudanmap.domain.entity.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Category(
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)