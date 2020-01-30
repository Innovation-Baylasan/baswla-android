package org.baylasan.sudanmap.domain.entity

import io.reactivex.Completable
import org.baylasan.sudanmap.data.entity.model.AddEntityRequest
import org.baylasan.sudanmap.domain.common.CompletableUseCase
import org.baylasan.sudanmap.domain.common.RequestValues

class AddEntityUseCase(private val repository: EntityRepository) :
    CompletableUseCase<AddEntityUseCase.Request> {
    class Request(val addRequest: AddEntityRequest) : RequestValues

    override fun execute(params: Request): Completable {
        return repository.addEntity(params.addRequest)
    }
}