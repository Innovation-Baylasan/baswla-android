package org.baylasan.sudanmap.data.user.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.baylasan.sudanmap.data.entity.model.EntityDetails

@Keep
data class Data(
    @SerializedName("token")
    val token: String = "",
    @SerializedName("user")
    val user: User = User(),

    @SerializedName("main_entity")
    val entity: EntityDetails? = null
)