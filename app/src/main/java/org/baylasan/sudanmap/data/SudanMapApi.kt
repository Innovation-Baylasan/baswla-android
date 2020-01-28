package org.baylasan.sudanmap.data

import io.reactivex.Single
import org.baylasan.sudanmap.data.category.model.CategoryDto
import org.baylasan.sudanmap.data.entity.EntityDetailsResponse
import org.baylasan.sudanmap.data.entity.EntityResponse
import org.baylasan.sudanmap.data.entity.model.Review
import org.baylasan.sudanmap.data.event.model.EventResponse
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.LoginResponse
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.data.user.model.RegisterResponse
import org.baylasan.sudanmap.domain.entity.AddReviewUseCase
import retrofit2.Response
import retrofit2.http.*

interface SudanMapApi {

    interface Entities {
        @GET("entities")
        fun getEntities(): Single<Response<EntityResponse>>

        @GET("categories/{id}/entities")
        fun getEntitiesByCategory(@Path("id") id: Int = 1): Single<Response<EntityResponse>>

        @GET("entities")
        fun getNearbyEntities(@Query("@lat") lat: Double, @Query("@long") long: Double):
                Single<Response<EntityResponse>>

        @GET("entities")
        fun findEntitiesByKeyword(@Query("q") keyword: String): Single<Response<EntityResponse>>

        @PUT("entities/{id}/rating")
        fun rateEntityById(@Path("id") entityId: Int, @Body rate: RatingRequest): Single<Response<Unit>>

        @POST("entities/{id}/review")
        fun reviewEntityById(@Path("id") entityId: Int, @Body request: AddReviewUseCase.Request): Single<Response<Review>>

        @POST("entities/{id}/follow")
        fun followEntity(@Path("id") entityId: Int): Single<Response<Unit>>

        @DELETE("entities/{id}/review")
        fun unFollowEntity(@Path("id") entityId: Int): Single<Response<Unit>>

        @GET("entities/{id}")
        fun entityDetails(@Path("id") entityId: Int): Single<Response<EntityDetailsResponse>>

    }

    interface Categories {
        @GET("categories")
        fun getCategories(): Single<Response<CategoryDto>>

        @GET("categories/{id}")
        fun getCategoryDetails(@Path("id") categoryId: Int)
    }

    interface User {
        @POST("register")
        fun register(@Body registerRequest: RegisterRequest): Single<Response<RegisterResponse>>

        @POST("login")
        fun login(@Body loginRequest: LoginRequest): Single<Response<LoginResponse>>
    }

    interface Events {
        @GET("events")
        fun events(): Single<Response<EventResponse>>
        @GET("events/my")
        fun myEvents(): Single<Response<EventResponse>>
    }
}

const val BASE_URL = "http://104.248.145.132/api/"