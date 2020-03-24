package org.baylasan.sudanmap.data

import io.reactivex.Single
import okhttp3.MultipartBody
import org.baylasan.sudanmap.data.category.model.CategoryDto
import org.baylasan.sudanmap.data.entity.model.*
import org.baylasan.sudanmap.data.event.model.AddEventResponse
import org.baylasan.sudanmap.data.event.model.EventResponse
import org.baylasan.sudanmap.data.tags.model.TagsResponse
import org.baylasan.sudanmap.data.user.model.AuthenticationResponse
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.RegisterRequest
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
        fun rateEntityById(@Path("id") entityId: Int, @Body rate: RatingRequest): Single<Response<RateResponse>>

        @POST("entities/{id}/review")
        fun reviewEntityById(@Path("id") entityId: Int, @Body request: AddReviewUseCase.Request): Single<Response<Review>>

        @POST("entities/{id}/follow")
        fun followEntity(@Path("id") entityId: Int): Single<Response<Unit>>

        @DELETE("entities/{id}/follow")
        fun unFollowEntity(@Path("id") entityId: Int): Single<Response<Unit>>

        @GET("entities/{id}")
        fun entityDetails(@Path("id") entityId: Int): Single<Response<EntityDetailsResponse>>

        @GET("entities/my")
        fun getMyEntities(): Single<Response<EntityResponse>>

        @Multipart
        @POST("entities")
        fun addEntity(@Part list: List<MultipartBody.Part>): Single<Response<Unit>>

        @GET("entities/{id}/related")
        fun getRelated(@Path("id") id: Int): Single<Response<List<Entity>>>

    }

    interface Categories {
        @GET("categories")
        fun getCategories(): Single<Response<CategoryDto>>

        @GET("categories/{id}")
        fun getCategoryDetails(@Path("id") categoryId: Int)
    }

    interface User {
        @POST("register")
        fun register(@Body registerRequest: RegisterRequest): Single<Response<AuthenticationResponse>>


        @POST("login")
        fun login(@Body loginRequest: LoginRequest): Single<Response<AuthenticationResponse>>
    }

    interface Events {
        @GET("events")
        fun events(): Single<Response<EventResponse>>

        @DELETE("events/destroy")
        fun deleteEvent(@Query("event") eventId: Int): Single<Response<Unit>>

        @GET("events/my")
        fun myEvents(): Single<Response<EventResponse>>

        @Multipart
        @POST("events")
        fun addEvent(@Part list: List<MultipartBody.Part>): Single<Response<AddEventResponse>>

        @GET("events")
        fun getEntityEvents(@Query("entity") entityId: Int): Single<Response<EventResponse>>

        @GET("events")
        fun findEvents(@Query("q") keyword: String): Single<Response<EventResponse>>
    }

    interface Tags {
        @GET("tags")
        fun getTags(): Single<Response<TagsResponse>>

        @GET("tags")
        fun getTagsByName(@Query("q") name: String): Single<Response<TagsResponse>>
    }
}

