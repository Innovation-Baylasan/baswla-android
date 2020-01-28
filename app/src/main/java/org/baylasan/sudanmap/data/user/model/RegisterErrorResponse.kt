package org.baylasan.sudanmap.data.user.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RegisterErrorResponse(
    @SerializedName("errors") val errors: Errors,
    var httpCode: Int
)

@Keep
data class Errors(
    @SerializedName("email") val email: List<String>?,
    @SerializedName("password") val password: List<String>?,
    @SerializedName("name") val username: List<String>?
)

fun Errors.stringify(): String {
    return listOfNotNull(email, password, username)
        .flatten().joinToString { "$it\n" }
}