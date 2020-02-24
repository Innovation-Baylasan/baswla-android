package org.baylasan.sudanmap.data.event.model

import com.google.gson.annotations.SerializedName

data class AddEventResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val event: Event? = null
)