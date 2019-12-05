package m7mdra.com.mawgif.domain.places

import io.reactivex.Single
import m7mdra.com.mawgif.domain.common.RequestValues
import m7mdra.com.mawgif.domain.common.SingleUseCase
import m7mdra.com.mawgif.domain.places.model.Place

class AvailablePlacesUseCase(private val placeRepository: PlaceRepository) :
        SingleUseCase<AvailablePlacesUseCase.Request, List<Place>> {
    override fun execute(params: Request): Single<List<Place>> {
        return placeRepository.getAvailablePlaces()
    }

    class Request : RequestValues
}