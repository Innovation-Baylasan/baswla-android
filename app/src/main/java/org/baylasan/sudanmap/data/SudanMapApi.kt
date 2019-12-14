package org.baylasan.sudanmap.data

import io.reactivex.Single
import org.baylasan.sudanmap.domain.entity.model.Category
import org.baylasan.sudanmap.domain.entity.model.EntityResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SudanMapApi {

    interface Entity {
        @GET("entities")
        fun getEntities(): Single<Response<EntityResponseDto>>

        @GET("categories/{id}/entities")
        fun getEntitiesByCategory(@Path("id") id: Int = 1): Single<Response<EntityResponseDto>>
    }

    interface Categories {
        @GET("categories")
        fun getCategories(): Single<Response<List<Category>>>

        @GET("categories/{id}")
        fun getCategoryDetails(@Path("id") categoryId: Int)
    }
}

const val BASE_URL = "http://104.248.145.132/api/"