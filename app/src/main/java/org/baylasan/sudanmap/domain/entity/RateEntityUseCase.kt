package org.baylasan.sudanmap.domain.entity


import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.RateResponse
import org.baylasan.sudanmap.data.entity.model.RatingRequest
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class RateEntityUseCase(private val repository: EntityRepository) :
    SingleUseCase<RateEntityUseCase.Request, RateResponse> {
    class Request(val entityId: Int, val rating: Double) : RequestValues

    override fun execute(params: Request): Single<RateResponse> {
        return repository.rateEntity(
            params.entityId,
            RatingRequest(params.rating)
        )
    }

}