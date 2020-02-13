package org.baylasan.sudanmap.data.entity.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Review(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("entity_id") val entityId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("writer") val name: String,
    @SerializedName("review") val review: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("user_id") val userId: Int
) : Parcelable