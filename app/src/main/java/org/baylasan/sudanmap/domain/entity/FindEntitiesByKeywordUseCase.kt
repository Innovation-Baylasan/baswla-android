package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class FindEntitiesByKeywordUseCase(private val entityRepository: EntityRepository) :
    SingleUseCase<FindEntitiesByKeywordUseCase.Request, List<Entity>> {
    class Request(val keyword: String) : RequestValues

    override fun execute(params: Request): Single<List<Entity>> {
        if (params.keyword.isEmpty())
            return Single.error(KeywordEmptyException())
        return entityRepository.findEntitiesByKeyword(params.keyword)
    }
}

class KeywordEmptyException : Exception("keyword can not be empty")