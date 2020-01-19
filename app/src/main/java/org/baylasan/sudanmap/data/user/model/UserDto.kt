package org.baylasan.sudanmap.data.user.model

import androidx.annotation.Keep

@Keep
data class UserDto(
    val email: String = "",
    val id: Int = 0,
    val name: String = "",
    val username: String = "",
    val token: String = ""
)