package org.baylasan.sudanmap.data.entity


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.baylasan.sudanmap.data.entity.model.EntityDetails

@Keep
data class EntityDetailsResponse(
    @SerializedName("data") val details: EntityDetails
)