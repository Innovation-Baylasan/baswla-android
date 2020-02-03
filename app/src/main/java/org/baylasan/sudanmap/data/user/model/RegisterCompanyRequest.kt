package org.baylasan.sudanmap.data.user.model


import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
@Keep
data class RegisterCompanyRequest(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val location: String,
    val categoryId: Int,
    val avatar: File?,
    val cover: File?,
    val description: String,
    val userType: String = "company"
) : Parcelable

