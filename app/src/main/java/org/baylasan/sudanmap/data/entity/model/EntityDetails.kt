package org.baylasan.sudanmap.data.entity.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class EntityDetails(
    @SerializedName("avatar") val avatar: String,
    @SerializedName("category") val category: Category,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("cover") val cover: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("description") val description: String,
    @SerializedName("details") val details: String,
    @SerializedName("followers_count") val followersCount: Int,
    @SerializedName("reviews_count") val reviewsCount: Int,
    @SerializedName("average_rating") val rating: String,
    @SerializedName("my_rating") val myRating: String,
    @SerializedName("current_following_status") val isFollowed: Boolean,

    @SerializedName("id") val id: Int,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("name") val name: String,
    @SerializedName("parent_id") val parentId: Int,
    @SerializedName("reviews") val reviews: List<Review>,
    @SerializedName("tags") val tags: List<Tag>,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("user_id") val userId: Int
)