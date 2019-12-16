package org.baylasan.sudanmap.domain.entity.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Entity(
    @SerializedName("avatar")
    val avatar: String = "",
    @SerializedName("cover")
    val cover: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("location")
    val location: Location = Location(),
    @SerializedName("name")
    val name: String = ""
)