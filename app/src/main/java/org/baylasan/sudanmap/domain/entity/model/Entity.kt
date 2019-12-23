package org.baylasan.sudanmap.domain.entity.model


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Entity(
    @SerializedName("avatar")
    val avatar: String = "",
    @SerializedName("cover")
    val cover: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("location")
    val location: Location = Location(),
    @SerializedName("name")
    val name: String = ""
):Parcelable