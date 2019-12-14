package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.domain.entity.model.EntityResponseDto

interface EntityRepository {
    fun getEntities(): Single<EntityResponseDto>
}