package org.baylasan.sudanmap.domain.entity

import io.reactivex.Completable
import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Entity

interface EntityRepository {
    fun getNearbyEntities(latitude: Double, longitude: Double): Single<List<Entity>>
    fun getEntities(): Single<List<Entity>>
    fun findEntitiesByKeyword(keyword: String): Single<List<Entity>>
    fun rateEntity(id: Int, rating: Double): Completable
    fun addReview(id: Int, comment: String): Completable
    fun follow(id: Int): Completable
    fun unFollow(id: Int): Completable
}