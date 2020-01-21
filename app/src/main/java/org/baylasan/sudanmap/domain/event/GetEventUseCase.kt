package org.baylasan.sudanmap.domain.event

import io.reactivex.Single
import org.baylasan.sudanmap.data.event.model.EventResponse
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase

class GetEventUseCase(private val repository: EventRepository) : EmptyRequestUseCase<EventResponse> {
    override fun execute(): Single<EventResponse> {
        return repository.getEvents()
    }
}