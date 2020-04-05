package org.baylasan.sudanmap.data.policy

import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.ResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import org.baylasan.sudanmap.domain.policy.PolicyRepository
import org.baylasan.sudanmap.domain.policy.model.Policy
import retrofit2.Converter

class PolicyApi(
    private val api: SudanMapApi.Miscs,
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>
) : PolicyRepository {
    override fun policy(): Single<Policy> {
        return api.policy()
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
    }
}