package org.baylasan.sudanmap.domain.event

import io.reactivex.Single
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class GetEntityEventsUseCase(private val repository: EventRepository) :
    SingleUseCase<GetEntityEventsUseCase.Request, List<Event>> {
    class Request(val id: Int) : RequestValues {

    }

    override fun execute(params: Request): Single<List<Event>> {
        return repository.getEntityEvents(params.id)
    }

}
