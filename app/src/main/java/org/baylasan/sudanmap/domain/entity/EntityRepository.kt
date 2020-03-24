package org.baylasan.sudanmap.domain.entity

import io.reactivex.Completable
import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.*

interface EntityRepository {
    fun getNearbyEntities(latitude: Double, longitude: Double): Single<List<Entity>>
    fun getEntities(): Single<List<Entity>>
    fun findEntitiesByKeyword(keyword: String): Single<List<Entity>>
    fun rateEntity(id: Int, rating: RatingRequest): Single<RateResponse>
    fun addReview(request: AddReviewUseCase.Request): Single<Review>
    fun follow(id: Int): Completable
    fun unFollow(id: Int): Completable
    fun getEntityDetails(id: Int): Single<EntityDetails>
    fun getMyEntities(): Single<List<Entity>>
    fun addEntity(addEntityRequest: AddEntityRequest): Completable
    fun getRelated(id: Int): Single<List<Entity>>
}