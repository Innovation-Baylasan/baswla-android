package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class GetRelatedEntityUseCase(private val repository: EntityRepository) :
    SingleUseCase<Request, List<Entity>> {

    override fun execute(params: Request): Single<List<Entity>> {
        return repository.getRelated(params.id)
    }
}

class Request(val id: Int) : RequestValues