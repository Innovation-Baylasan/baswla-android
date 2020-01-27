package org.baylasan.sudanmap.domain.entity

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Review
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class AddReviewUseCase(private val repository: EntityRepository) :
    SingleUseCase<AddReviewUseCase.Request, Review> {
    class Request(val entityId: Int,
                  @SerializedName("review") val review: String) :
        RequestValues


    override fun execute(params: Request): Single<Review> {
        return repository.addReview(params)
    }

}