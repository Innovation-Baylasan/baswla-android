package org.baylasan.sudanmap.domain.event

import io.reactivex.Completable
import org.baylasan.sudanmap.domain.common.CompletableUseCase
import org.baylasan.sudanmap.domain.common.RequestValues

class DeleteEventUseCase(private val repository: EventRepository) : CompletableUseCase<Request> {
    override fun execute(params: Request): Completable {
        return repository.deleteEvent(params.eventId)
    }
}

class Request(val eventId: Int) : RequestValues {

}
