package org.baylasan.sudanmap.data.event

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import org.baylasan.sudanmap.data.event.model.AddEventResponseError
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.data.event.model.EventResponse
import org.baylasan.sudanmap.domain.event.EventRepository
import retrofit2.Converter

class EventApi(
    private val errorConverter: Converter<ResponseBody, ApiErrorResponse>,
    private val addEventErrorConverter: Converter<ResponseBody, AddEventResponseError>,
    private val api: SudanMapApi.Events,
    private val requestMapper: RequestMapper<AddEventRequest>
) : EventRepository {
    override fun getEvents(): Single<List<Event>> {
        return api.events()
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1<EventResponse>(errorConverter))
            .map { it.events }

    }

    override fun deleteEvent(eventId: Int): Completable {
        return api.deleteEvent(eventId)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .ignoreElement()
    }

    override fun findEvent(keyword: String): Single<List<Event>> {

        return api.findEvents(keyword)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1<EventResponse>(errorConverter))
            .map { it.events }
    }

    override fun getMyEvents(): Single<List<Event>> {
        return api.myEvents()
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it.events }
    }

    override fun addEvent(addEventRequest: AddEventRequest): Single<Event> {
        return api.addEvent(requestMapper.mapToResponseBody(addEventRequest))
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(AddEventResponseSingleFunc1(addEventErrorConverter))
            .map { it.event }
    }

    override fun getEntityEvents(entityId: Int): Single<List<Event>> {
        return api.getEntityEvents(entityId)
            .onErrorResumeNext(ThrowableSingleFunc1())
            .flatMap(ResponseSingleFunc1(errorConverter))
            .map { it.events }
    }

}