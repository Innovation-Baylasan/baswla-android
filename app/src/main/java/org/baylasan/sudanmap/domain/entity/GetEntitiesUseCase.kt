package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase
import org.baylasan.sudanmap.domain.entity.model.EntityDto

class GetEntitiesUseCase(private val entityRepository: EntityRepository) :
    SingleUseCase<GetEntitiesUseCase.Params, List<EntityDto>> {

    class Params(val categoryId: Int) : RequestValues

    override fun execute(params: Params): Single<List<EntityDto>> {
        return  entityRepository.getEntities(params.categoryId)
    }
}
