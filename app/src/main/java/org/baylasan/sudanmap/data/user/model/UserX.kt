package org.baylasan.sudanmap.data.user.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserX(
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("role")
    val role: Role = Role(),
    @SerializedName("role_id")
    val roleId: Int = 0,
    @SerializedName("updated_at")
    val updatedAt: String = "",
    @SerializedName("username")
    val username: String = ""
)