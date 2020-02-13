package org.baylasan.sudanmap.domain.event

import io.reactivex.Completable
import io.reactivex.Single
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import org.baylasan.sudanmap.data.event.model.Event

interface EventRepository {
    fun getEvents(): Single<List<Event>>
    fun deleteEvent(eventId: Int): Completable
    fun findEvent(keyword: String): Single<List<Event>>
    fun getMyEvents(): Single<List<Event>>
    fun addEvent(addEventRequest: AddEventRequest): Single<Event>
    fun getEntityEvents(entityId: Int): Single<List<Event>>

}