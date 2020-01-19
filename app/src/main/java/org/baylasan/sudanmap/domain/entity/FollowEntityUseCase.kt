package org.baylasan.sudanmap.domain.entity

import io.reactivex.Completable
import org.baylasan.sudanmap.domain.common.CompletableUseCase
import org.baylasan.sudanmap.domain.common.RequestValues


class FollowEntityUseCase(private val repository: EntityRepository) : CompletableUseCase<FollowEntityUseCase.Request> {
    override fun execute(params: Request): Completable {
        return repository.follow(params.entityId)
    }
class Request(val entityId: Int) : RequestValues
}

