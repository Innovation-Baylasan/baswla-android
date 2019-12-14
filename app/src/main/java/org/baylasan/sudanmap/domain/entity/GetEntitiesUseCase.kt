package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase
import org.baylasan.sudanmap.domain.entity.model.EntityResponseDto

class GetEntitiesUseCase(private val entityRepository: EntityRepository) :
    EmptyRequestUseCase<EntityResponseDto> {

    //  class Params(val categoryId: Int) : RequestValues

    override fun execute(): Single<EntityResponseDto> {
        return entityRepository.getEntities()
    }
}
