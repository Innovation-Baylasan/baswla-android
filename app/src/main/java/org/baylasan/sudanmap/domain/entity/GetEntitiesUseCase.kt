package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase
import org.baylasan.sudanmap.domain.entity.model.EntityDto

class GetEntitiesUseCase(private val entityRepository: EntityRepository) :
    SingleUseCase<GetEntitiesUseCase.Params, List<EntityDto>> {

    val categoryId: Int = 1
    override fun execute(params: Params): Single<List<EntityDto>> {
        return entityRepository.getEntities(params.categoryId)
    }

    class Params(val categoryId: Int) : RequestValues
}
