package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.domain.entity.model.EntityDto

interface EntityRepository {
    fun getEntities(params: Int):Single<List<EntityDto>>
}