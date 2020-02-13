package org.baylasan.sudanmap.domain.event

import io.reactivex.Single
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class AddEventUseCase(private val eventRepository: EventRepository) :
    SingleUseCase<AddEventUseCase.Request, Event> {
    class Request(val eventRequest: AddEventRequest) : RequestValues

    override fun execute(params: Request): Single<Event> {
        return eventRepository.addEvent(params.eventRequest)
    }
}