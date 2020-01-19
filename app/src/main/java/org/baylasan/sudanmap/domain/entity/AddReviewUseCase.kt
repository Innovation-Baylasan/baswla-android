package org.baylasan.sudanmap.domain.entity

import io.reactivex.Completable
import org.baylasan.sudanmap.domain.common.CompletableUseCase
import org.baylasan.sudanmap.domain.common.RequestValues

class AddReviewUseCase(private val repository: EntityRepository) :
    CompletableUseCase<AddReviewUseCase.Request> {
    class Request(val entityId: Int, val review: String) : RequestValues

    override fun execute(params: Request): Completable {
        return repository.addReview(params.entityId, params.review)
    }

}