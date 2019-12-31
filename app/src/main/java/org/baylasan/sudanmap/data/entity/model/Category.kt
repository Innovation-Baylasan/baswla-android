package org.baylasan.sudanmap.data.entity.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Category(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("updated_at") val updatedAt: String
) : Parcelable {
    constructor() : this("", "", -1, "ALL", "")
}