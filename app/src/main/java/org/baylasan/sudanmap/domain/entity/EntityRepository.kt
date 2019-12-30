package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.EntityDto
import org.baylasan.sudanmap.data.entity.model.NearByEntity

interface EntityRepository {
    fun getEntities(): Single<List<EntityDto>>
    fun getNearbyEntities(latitude: Double, longitude: Double): Single<NearByEntity>
}