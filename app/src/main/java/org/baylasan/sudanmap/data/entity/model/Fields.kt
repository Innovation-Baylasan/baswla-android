package org.baylasan.sudanmap.data.entity.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Fields(
    @SerializedName("category_id") val categoryId: List<String>,
    @SerializedName("description") val description: List<String>,
    @SerializedName("latitude") val latitude: List<String>,
    @SerializedName("longitude") val longitude: List<String>,
    @SerializedName("name") val name: List<String>
)

fun Fields.stringify(): String {
    return listOfNotNull(categoryId, description, longitude, latitude, name)
        .flatten().joinToString { "$it\n" }
}
