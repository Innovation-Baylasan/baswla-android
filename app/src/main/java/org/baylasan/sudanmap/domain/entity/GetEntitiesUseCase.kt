package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase

class GetEntitiesUseCase(private val entityRepository: EntityRepository) :
    EmptyRequestUseCase<List<Entity>> {

    //  class Params(val categoryId: Int) : RequestValues

    override fun execute(): Single<List<Entity>> {
        return entityRepository.getEntities()
    }
}
