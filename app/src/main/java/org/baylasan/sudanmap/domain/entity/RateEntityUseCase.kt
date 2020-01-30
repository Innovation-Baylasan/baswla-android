package org.baylasan.sudanmap.domain.entity


import io.reactivex.Completable
import org.baylasan.sudanmap.data.user.model.RatingRequest
import org.baylasan.sudanmap.domain.common.CompletableUseCase
import org.baylasan.sudanmap.domain.common.RequestValues

class RateEntityUseCase(private val repository: EntityRepository) :
    CompletableUseCase<RateEntityUseCase.Request> {
    class Request(val entityId: Int, val rating: Double) : RequestValues

    override fun execute(params: Request): Completable {

        return repository.rateEntity(params.entityId,
            RatingRequest(params.rating)
        )
    }

}