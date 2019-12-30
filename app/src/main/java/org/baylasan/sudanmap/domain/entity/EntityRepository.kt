package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.domain.entity.model.Entity

interface EntityRepository {
    fun getEntities(): Single<List<Entity>>
    fun findEntitiesByKeyword(keyword: String): Single<List<Entity>>
}