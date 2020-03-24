package org.baylasan.sudanmap.domain.tags

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Tag
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase

class GetTagsUseCase(private val repository: TagsRepository) : EmptyRequestUseCase<List<Tag>>{
    override fun execute(): Single<List<Tag>> {
        return repository.getTags()
    }

}