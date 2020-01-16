package org.baylasan.sudanmap.data.user.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RegisterResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = ""
)