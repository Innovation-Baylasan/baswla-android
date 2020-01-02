package org.baylasan.sudanmap.data

import io.reactivex.Single
import org.baylasan.sudanmap.data.category.model.CategoryDto
import org.baylasan.sudanmap.data.entity.EntityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    }

    interface Categories {
        @GET("categories")
        fun getCategories(): Single<Response<CategoryDto>>

        @GET("categories/{id}")
        fun getCategoryDetails(@Path("id") categoryId: Int)
    }
}

const val BASE_URL = "http://104.248.145.132/api/"