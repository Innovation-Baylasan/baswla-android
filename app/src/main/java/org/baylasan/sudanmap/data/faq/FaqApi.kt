package org.baylasan.sudanmap.data.faq

import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.ResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import org.baylasan.sudanmap.domain.faq.FaqRepository
import org.baylasan.sudanmap.domain.faq.model.Faqs
import retrofit2.Converter

class FaqApi(
    private val api: SudanMapApi.Miscs,
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>
) : FaqRepository {
    override fun faq(): Single<Faqs> {
        return api.faqs()
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
    }
}