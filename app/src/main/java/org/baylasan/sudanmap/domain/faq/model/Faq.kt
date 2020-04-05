package org.baylasan.sudanmap.domain.faq.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

typealias FaqData = Data

@Keep
data class Faq(
    @SerializedName("data") val data: List<Data>
){
}