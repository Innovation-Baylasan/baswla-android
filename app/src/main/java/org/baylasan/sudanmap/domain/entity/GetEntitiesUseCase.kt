package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.common.EmptyRequestUseCase

class GetEntitiesUseCase (private val repository: EntityRepository): EmptyRequestUseCase<List<Entity>> {
    override fun execute(): Single<List<Entity>> {
        return repository.getEntities()
    }
}