package org.baylasan.sudanmap.data.entity.model


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class EntityDetails(
    @SerializedName("avatar") val avatar: String = "",
    @SerializedName("category") val category: Category = Category(),
    @SerializedName("category_id") val categoryId: Int = -1,
    @SerializedName("cover") val cover: String = "",
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("details") val details: String = "",
    @SerializedName("followers_count") val followersCount: Int = 0,
    @SerializedName("reviews_count") val reviewsCount: Int = 0,
    @SerializedName("average_rating") val rating: String = "0",
    @SerializedName("my_rating") val myRating: String = "",
    @SerializedName("current_following_status") val isFollowed: Boolean = false,
    @SerializedName("id") val id: Int = -1,
    @SerializedName("latitude") val latitude: Double = 0.0,
    @SerializedName("longitude") val longitude: Double = 0.0,
    @SerializedName("name") val name: String = "",
    @SerializedName("parent_id") val parentId: Int = -1,
    @SerializedName("reviews") val reviews: List<Review> = listOf(),
    @SerializedName("tags") val tags: List<Tag> = listOf(),
    @SerializedName("updated_at") val updatedAt: String = "",
    @SerializedName("user_id") val userId: Int = -1
) : Parcelable