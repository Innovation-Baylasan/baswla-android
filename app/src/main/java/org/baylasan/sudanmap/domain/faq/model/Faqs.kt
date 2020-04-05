package org.baylasan.sudanmap.domain.faq.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class Faqs(
    @SerializedName("data") val data: List<Faq>
) {

}