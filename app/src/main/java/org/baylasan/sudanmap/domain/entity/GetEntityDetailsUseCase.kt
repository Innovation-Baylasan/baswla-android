package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.EntityDetails
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class GetEntityDetailsUseCase(private val repository: EntityRepository) :
    SingleUseCase<GetEntityDetailsUseCase.Request, EntityDetails> {
    class Request(val id: Int) : RequestValues

    override fun execute(params: Request): Single<EntityDetails> {
        return repository.getEntityDetails(params.id)
    }
}