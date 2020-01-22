package org.baylasan.sudanmap.data.entity.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Review(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("entity_id") val entityId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("review") val review: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("user_id") val userId: Int
)