package org.baylasan.sudanmap.domain.event

import io.reactivex.Single
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class FindEventUseCase(private val repository: EventRepository) :
    SingleUseCase<FindEventUseCase.Request, List<Event>> {

    class Request(val keyword: String) : RequestValues

    override fun execute(params: Request): Single<List<Event>> {
        return repository.findEvent(params.keyword)
    }


}

