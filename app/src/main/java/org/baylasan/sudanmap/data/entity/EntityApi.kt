package org.baylasan.sudanmap.data.entity

import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.domain.entity.EntityRepository
import org.baylasan.sudanmap.domain.entity.model.EntityDto
import retrofit2.Converter

class EntityApi(
    private val entityApi: EntityApi,
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>
) :
    EntityRepository {
    override fun getEntities(params: Int): Single<List<EntityDto>> = entityApi.getEntities(params)
}
