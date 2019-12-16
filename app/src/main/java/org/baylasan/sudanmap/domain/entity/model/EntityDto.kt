package org.baylasan.sudanmap.domain.entity.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class EntityDto(
    @SerializedName("category")
    val category: Category = Category(),
    @SerializedName("category_id")
    val categoryId: Int = 0,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("location")
    val location: Location = Location(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("profile_id")
    val profileId: Any? = null,
    @SerializedName("updated_at")
    val updatedAt: String = "",
    @SerializedName("user_id")
    val userId: Int = 0,
    val avatar: String,
    val cover: String
)