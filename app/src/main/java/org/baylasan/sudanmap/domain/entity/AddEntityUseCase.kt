package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.AddEntityRequest
import org.baylasan.sudanmap.data.entity.model.AddEntityResponse
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class AddEntityUseCase(private val repository: EntityRepository) :
    SingleUseCase<AddEntityUseCase.Request, Entity> {
    class Request(val addRequest: AddEntityRequest) : RequestValues

    override fun execute(params: Request): Single<Entity> {
        return repository.addEntity(params.addRequest)
    }
}