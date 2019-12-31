package org.baylasan.sudanmap.domain.entity

import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class GetNearbyEntitiesUseCase(private val entityRepository: EntityRepository) :
    SingleUseCase<GetNearbyEntitiesUseCase.Params, List<Entity>> {

    class Params(val latitude: Double, var longitude: Double) : RequestValues

    override fun execute(params: Params): Single<List<Entity>> {
        return entityRepository.getNearbyEntities(
            latitude = params.latitude,
            longitude = params.longitude
        )
    }


}