package org.baylasan.sudanmap.data.category

import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.ResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import org.baylasan.sudanmap.domain.category.CategoryRepository
import org.baylasan.sudanmap.domain.entity.model.Category
import retrofit2.Converter

class CategoryApi(
    private val sudanMapApi: SudanMapApi.Categories,
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>
) : CategoryRepository {
    override fun getCategories(): Single<List<Category>> {
        return sudanMapApi.getCategories()
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it.categories }
    }

}