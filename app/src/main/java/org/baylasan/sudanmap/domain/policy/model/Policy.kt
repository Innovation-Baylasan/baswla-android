package org.baylasan.sudanmap.domain.policy.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Policy(
    @SerializedName("data") val data: Data
)