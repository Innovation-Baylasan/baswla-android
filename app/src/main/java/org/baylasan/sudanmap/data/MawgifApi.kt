package m7mdra.com.mawgif.data

import io.reactivex.Single
import m7mdra.com.mawgif.domain.places.model.Place
import m7mdra.com.mawgif.domain.user.CreateAccountUseCase
import m7mdra.com.mawgif.domain.user.LoginUseCase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MawgifApi {
    interface User {
        @POST("users")
        fun createUser(@Body request: CreateAccountUseCase.Request): Single<Response<m7mdra.com.mawgif.domain.user.model.User>>

        @POST("users/login")
        fun login(@Body request: LoginUseCase.Request): Single<Response<m7mdra.com.mawgif.domain.user.model.User>>
    }

    interface Booking

    interface Places {
        @GET("places/availablePlaces")
        fun getAvailablePlaces(): Single<Response<List<Place>>>
    }
}