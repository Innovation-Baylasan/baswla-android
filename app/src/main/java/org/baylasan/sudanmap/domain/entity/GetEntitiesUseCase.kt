package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase
import org.baylasan.sudanmap.domain.entity.model.Entity

class GetEntitiesUseCase(private val entityRepository: EntityRepository) :
    EmptyRequestUseCase<List<Entity>> {

    //  class Params(val categoryId: Int) : RequestValues

    override fun execute(): Single<List<Entity>> {
        return entityRepository.getEntities()
    }
}
