package org.baylasan.sudanmap.data.user.model


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class RegisterRequest(
    @SerializedName("email")
    val email: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("password_confirmation")
    val passwordConfirmation: String = "",
    @SerializedName("role_id")
    val type: Int = 1
) : Parcelable

