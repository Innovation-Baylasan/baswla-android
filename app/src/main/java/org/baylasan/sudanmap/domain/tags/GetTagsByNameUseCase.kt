package org.baylasan.sudanmap.domain.tags

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Tag
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class GetTagsByNameUseCase(private val repository: TagsRepository) :
    SingleUseCase<GetTagsByNameUseCase.Request, List<Tag>> {
    class Request(val name: String) : RequestValues

    override fun execute(params: Request): Single<List<Tag>> {
        return repository.getTagsByName(params.name)
    }
}