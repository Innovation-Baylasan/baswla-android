package org.baylasan.sudanmap.domain.event

import io.reactivex.Completable
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import org.baylasan.sudanmap.domain.common.CompletableUseCase
import org.baylasan.sudanmap.domain.common.RequestValues

class AddEventUseCase(private val eventRepository: EventRepository) :
    CompletableUseCase<AddEventUseCase.Request> {
    class Request(val eventRequest: AddEventRequest) : RequestValues

    override fun execute(params: Request): Completable {
        return eventRepository.addEvent(params.eventRequest)
    }
}