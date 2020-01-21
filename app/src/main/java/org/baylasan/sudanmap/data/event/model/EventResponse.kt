package org.baylasan.sudanmap.data.event.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class EventResponse(
    @SerializedName("data") val events: List<Event>
)