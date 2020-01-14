package org.baylasan.sudanmap.data.user.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Data(
    @SerializedName("token")
    val token: String = "",
    @SerializedName("user")
    val user: User = User()
)