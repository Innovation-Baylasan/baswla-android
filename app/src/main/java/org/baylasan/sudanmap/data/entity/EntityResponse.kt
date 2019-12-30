package org.baylasan.sudanmap.data.entity

import com.google.gson.annotations.SerializedName
import org.baylasan.sudanmap.domain.entity.model.Entity

class EntityResponse(
    @SerializedName("data") val entityList: List<Entity>
)