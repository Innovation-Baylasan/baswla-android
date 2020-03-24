package org.baylasan.sudanmap.data.tags

import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.ResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import org.baylasan.sudanmap.data.entity.model.Tag
import org.baylasan.sudanmap.domain.tags.TagsRepository
import retrofit2.Converter

class TagsApi(
    private val api: SudanMapApi.Tags,
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>
) : TagsRepository {
    override fun getTagsByName(name: String): Single<List<Tag>> {
        return api.getTagsByName(name)

            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it.tagList }
    }

    override fun getTags(): Single<List<Tag>> {
        return api.getTags()
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it.tagList }
    }
}