package org.baylasan.sudanmap.data.event

import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.ResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import org.baylasan.sudanmap.data.event.model.EventResponse
import org.baylasan.sudanmap.domain.event.EventRepository
import retrofit2.Converter

class EventApi(
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>,
    private val api: SudanMapApi.Events
) : EventRepository {
    override fun getEvents(): Single<EventResponse> {
        return api.events()
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))

    }

}