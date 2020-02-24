package org.baylasan.sudanmap.data.entity.model

import com.google.gson.annotations.SerializedName
import java.io.File

class AddEntityRequest(
    val category: Int = 1,
    val name: String = "mega",
    val cover: File ?= null,
    val avatar: File? = null,
    val tags:Set<String> = setOf(),
    val description: String = "lawl",
    val locationLat: String = "1.1",
    val locationLng: String = "1.2"
)