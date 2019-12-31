package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Entity

interface EntityRepository {
    fun getNearbyEntities(latitude: Double, longitude: Double): Single<List<Entity>>
    fun getEntities(): Single<List<Entity>>
    fun findEntitiesByKeyword(keyword: String): Single<List<Entity>>
}