package m7mdra.com.mawgif.data.place

import io.reactivex.Single
import m7mdra.com.mawgif.data.MawgifApi
import m7mdra.com.mawgif.data.common.ApiErrorResponse
import m7mdra.com.mawgif.data.common.ResponseSingleFunc1
import m7mdra.com.mawgif.data.common.ThrowableSingleFunc1
import m7mdra.com.mawgif.domain.places.model.Place
import m7mdra.com.mawgif.domain.places.PlaceRepository
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.common.ResponseSingleFunc1
import org.baylasan.sudanmap.data.common.ThrowableSingleFunc1
import retrofit2.Converter
import javax.inject.Inject

class PlaceApi @Inject constructor(private val placeApi: MawgifApi.Places,
                                   private val errorConverter: Converter<ResponseBody, ApiErrorResponse>) : PlaceRepository {

    override fun getAvailablePlaces(): Single<List<Place>> {
        return placeApi.getAvailablePlaces()
                .onErrorResumeNext(ThrowableSingleFunc1())
                .flatMap(ResponseSingleFunc1(errorConverter))
    }

}