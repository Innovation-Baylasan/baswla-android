package org.baylasan.sudanmap.domain.event

import io.reactivex.Single
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase

class GetEventUseCase(private val repository: EventRepository) : EmptyRequestUseCase<List<Event>> {
    override fun execute(): Single<List<Event>> {
        return repository.getEvents()
    }
}