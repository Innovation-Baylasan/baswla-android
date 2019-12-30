package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.EntityDto
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase

class GetEntitiesUseCase(private val entityRepository: EntityRepository) :
    EmptyRequestUseCase<List<EntityDto>> {

    //  class Params(val categoryId: Int) : RequestValues

    override fun execute(): Single<List<EntityDto>> {
        return entityRepository.getEntities()
    }
}
