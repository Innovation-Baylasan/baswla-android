package org.baylasan.sudanmap.data.category.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.baylasan.sudanmap.domain.entity.model.Category

@Keep
data class CategoryDto(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("data") val categories: List<Category>,
    @SerializedName("first_page_url") val firstPageUrl: String,
    @SerializedName("from") val from: Int,
    @SerializedName("last_page") val lastPage: Int,
    @SerializedName("last_page_url") val lastPageUrl: String,
    @SerializedName("next_page_url") val nextPageUrl: String,
    @SerializedName("path") val path: String,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("prev_page_url") val prevPageUrl: Any?,
    @SerializedName("to") val to: Int,
    @SerializedName("total") val total: Int
) {
    constructor() : this(0, listOf(), "", 0, 0, "", "", "", 1, "", 1, 2) {}
}