package org.baylasan.sudanmap.data.entity.model

import com.google.gson.annotations.SerializedName
import java.io.File

class AddEntityRequest(
    @SerializedName("category_id")
    val category: Int = 1,
    @SerializedName("name")
    val name: String = "mega",
    @SerializedName("cover")
    val cover: File ?= null,
    @SerializedName("avatar")
    val avatar: File? = null,
    @SerializedName("description")
    val description: String = "lawl",
    @SerializedName("latitude")
    val locationLat: String = "1.1",
    @SerializedName("longitude")
    val locationLng: String = "1.2"
)