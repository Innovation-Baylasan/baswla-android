package org.baylasan.sudanmap.data.entity

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.entity.model.*
import org.baylasan.sudanmap.domain.entity.AddReviewUseCase
import org.baylasan.sudanmap.domain.entity.EntityRepository
import retrofit2.Converter

class EntityApi(
    private val entityApi: SudanMapApi.Entities,
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>,
    private val addErrorConverter: Converter<ResponseBody, AddEntityResponseError>,
    private val requestMapper: AddEntityRequestMapper
) :
    EntityRepository {
    override fun getEntities(): Single<List<Entity>> = entityApi.getEntities()
        .onErrorResumeNext(ThrowableSingleFunc1())
        .flatMap(ResponseSingleFunc1(errorConverter))
        .map { it.entityList }


    override fun getNearbyEntities(latitude: Double, longitude: Double) =
        entityApi.getNearbyEntities(latitude, longitude)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it.entityList }


    override fun findEntitiesByKeyword(keyword: String): Single<List<Entity>> {
        return entityApi.findEntitiesByKeyword(keyword)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it.entityList }
    }

    override fun rateEntity(id: Int, rating: RatingRequest): Single<RateResponse> {
        return entityApi.rateEntityById(id, rating)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
    }

    override fun addReview(request: AddReviewUseCase.Request): Single<Review> {
        return entityApi.reviewEntityById(request.entityId, request)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
    }

    override fun follow(id: Int): Completable {
        return entityApi.followEntity(id)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .ignoreElement()
    }

    override fun unFollow(id: Int): Completable {
        return entityApi.unFollowEntity(id)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .ignoreElement()
    }

    override fun getEntityDetails(id: Int): Single<EntityDetails> {
        return entityApi.entityDetails(id)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it.details }
    }

    override fun getMyEntities(): Single<List<Entity>> {
        return entityApi.getMyEntities()
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it.entityList }
    }

    override fun addEntity(addEntityRequest: AddEntityRequest): Completable {
        return entityApi.addEntity(requestMapper.mapToResponseBody(addEntityRequest))
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(AddEntityResponseSingleFunc1(addErrorConverter))
            .ignoreElement()
    }

    override fun getRelated(id: Int): Single<List<Entity>> {
        return entityApi.getRelated(id)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(AddEntityResponseSingleFunc1(addErrorConverter))
    }
}
