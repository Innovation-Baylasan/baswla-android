package org.baylasan.sudanmap.data.tags.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import org.baylasan.sudanmap.data.entity.model.Tag

@Keep
data class TagsResponse(
    @SerializedName("data") val tagList: List<Tag>
)