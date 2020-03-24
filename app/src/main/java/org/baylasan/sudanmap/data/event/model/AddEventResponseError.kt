package org.baylasan.sudanmap.data.event.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AddEventResponseError(
    @SerializedName("errors") val errors: Errors,
    var httpCode: Int
) {
}

fun Errors.stringify(): String {
    return listOfNotNull(endDate, startDate, latitude, longitude, link, name)
        .flatten()
        .joinToString { "$it\n" }
}
