package org.baylasan.sudanmap.domain.faq.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Faq(
    @SerializedName("answer") val answer: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("updated_at") val updatedAt: String
)