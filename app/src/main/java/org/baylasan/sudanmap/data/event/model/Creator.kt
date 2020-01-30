package org.baylasan.sudanmap.data.event.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Creator(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("email") val email: String,
    @SerializedName("email_verified_at") val emailVerifiedAt: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String,
    @SerializedName("role_id") val roleId: Int,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("username") val username: String
) :Parcelable