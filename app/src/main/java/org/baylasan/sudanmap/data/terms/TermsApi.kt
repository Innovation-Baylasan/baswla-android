package org.baylasan.sudanmap.data.terms

import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.ResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import org.baylasan.sudanmap.domain.terms.TermsRepository
import org.baylasan.sudanmap.domain.terms.model.Terms
import retrofit2.Converter

class TermsApi(
    private val api: SudanMapApi.Miscs,
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>
) : TermsRepository {
    override fun terms(): Single<Terms> {
        return api.terms()
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
    }
}