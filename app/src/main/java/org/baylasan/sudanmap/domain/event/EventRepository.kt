package org.baylasan.sudanmap.domain.event

import io.reactivex.Single
import org.baylasan.sudanmap.data.event.model.EventResponse

interface EventRepository {
    fun getEvents(): Single<EventResponse>
}