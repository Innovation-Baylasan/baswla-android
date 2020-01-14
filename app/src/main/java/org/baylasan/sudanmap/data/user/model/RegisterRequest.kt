package org.baylasan.sudanmap.data.user.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RegisterRequest(
    @SerializedName("email")
    val email: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("password_confirmation")
    val passwordConfirmation: String = "",
    @SerializedName("role_id")
    val roleId: Int = 0/*,
    @SerializedName("username")
    val username: String = ""*/
)