package org.baylasan.sudanmap.data.user.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.baylasan.sudanmap.data.entity.model.EntityDetails

@Keep
data class AuthenticationResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = ""
)