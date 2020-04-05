package org.baylasan.sudanmap.domain.terms.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import org.baylasan.sudanmap.domain.policy.model.Data

@Keep
data class Terms(
    @SerializedName("data") val data: Data
)