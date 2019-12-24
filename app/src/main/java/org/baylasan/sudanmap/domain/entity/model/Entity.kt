package org.baylasan.sudanmap.domain.entity.model


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Entity(
    @SerializedName("avatar") val avatar: String,
    @SerializedName("category") val category: Category,
    @SerializedName("cover") val cover: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: Int,
    @SerializedName("location") val location: Location,
    @SerializedName("name") val name: String
):Parcelable