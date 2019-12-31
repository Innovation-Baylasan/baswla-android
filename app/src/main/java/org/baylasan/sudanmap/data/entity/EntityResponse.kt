package org.baylasan.sudanmap.data.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.baylasan.sudanmap.data.entity.model.Entity
@Keep
class EntityResponse(
    @SerializedName("data") val entityList: List<Entity>
)