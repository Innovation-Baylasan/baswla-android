package org.baylasan.sudanmap.data.entity.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Tag(
    @SerializedName("id") val id: Int,
    @SerializedName("label") val label: String
) : Parcelable