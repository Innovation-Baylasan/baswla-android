package org.baylasan.sudanmap.data.entity.model

import com.google.gson.annotations.SerializedName

data class AddEntityResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val entity: Entity? = null
)