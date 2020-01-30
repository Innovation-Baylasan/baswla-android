package org.baylasan.sudanmap.domain.entity

import io.reactivex.Completable
import io.reactivex.Single
import org.baylasan.sudanmap.data.user.model.RatingRequest
import org.baylasan.sudanmap.data.entity.model.AddEntityRequest
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.data.entity.model.EntityDetails
import org.baylasan.sudanmap.data.entity.model.Review

interface EntityRepository {
    fun getNearbyEntities(latitude: Double, longitude: Double): Single<List<Entity>>
    fun getEntities(): Single<List<Entity>>
    fun findEntitiesByKeyword(keyword: String): Single<List<Entity>>
    fun rateEntity(id: Int, rating: RatingRequest): Completable
    fun addReview(request: AddReviewUseCase.Request): Single<Review>
    fun follow(id: Int): Completable
    fun unFollow(id: Int): Completable
    fun getEntityDetails(id: Int): Single<EntityDetails>
    fun getMyEntities(): Single<List<Entity>>
    fun addEntity(addEntityRequest: AddEntityRequest): Completable
}