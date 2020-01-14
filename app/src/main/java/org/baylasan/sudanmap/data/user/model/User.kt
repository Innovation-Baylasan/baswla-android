package org.baylasan.sudanmap.data.user.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class User(
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = "",
    @SerializedName("username")
    val username: String = ""
)