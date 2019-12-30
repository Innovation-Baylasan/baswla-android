package org.baylasan.sudanmap.data.entity

import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.ResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import org.baylasan.sudanmap.data.entity.model.EntityDto
import org.baylasan.sudanmap.domain.entity.EntityRepository
import retrofit2.Converter

class EntityApi(
    private val entityApi: SudanMapApi.Entity,
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>
) :
    EntityRepository {
    override fun getEntities(): Single<List<EntityDto>> = entityApi.getEntities()
        .onErrorResumeNext(ThrowableSingleFunc1())
        .flatMap(ResponseSingleFunc1(errorConverter))


    override fun getNearbyEntities(latitude: Double, longitude: Double) =
        entityApi.getNearbyEntities(latitude, longitude)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))

}
