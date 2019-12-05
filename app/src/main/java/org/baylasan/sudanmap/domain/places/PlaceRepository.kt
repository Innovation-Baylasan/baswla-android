package m7mdra.com.mawgif.domain.places

import io.reactivex.Single
import m7mdra.com.mawgif.domain.places.model.Place

interface PlaceRepository {
    fun getAvailablePlaces(): Single<List<Place>>
}