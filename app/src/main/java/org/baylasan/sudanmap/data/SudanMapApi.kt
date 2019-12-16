package org.baylasan.sudanmap.data

import io.reactivex.Single
import org.baylasan.sudanmap.data.category.model.CategoryDto
import org.baylasan.sudanmap.domain.entity.model.Category
import org.baylasan.sudanmap.domain.entity.model.EntityDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SudanMapApi {
    /* interface User {
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
 */

    interface Entity {
        @GET("categories/{id}/entities")
        fun getEntities(@Path("id") id: Int = 1): Single<Response<List<EntityDto>>>
    }

    interface Categories {
        @GET("categories")
        fun getCategories(): Single<Response<CategoryDto>>

        @GET("categories/{id}")
        fun getCategoryDetails(@Path("id") categoryId: Int)
    }
}

const val BASE_URL = "http://104.248.145.132/api/"