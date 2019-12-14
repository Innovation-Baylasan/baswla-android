package org.baylasan.sudanmap.data.category

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.category.model.CategoryDto
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import retrofit2.Response

class CategoryApiTest {
    private lateinit var categoryService: SudanMapApi.Categories
    private lateinit var converter: Converter<ResponseBody, ApiErrorResponse>
    private lateinit var categoryApi: CategoryApi

    @Before
    fun setup() {
        categoryService = mock(verboseLogging = true)
        converter = mock(verboseLogging = true)
        categoryApi = CategoryApi(categoryService, converter)

    }

    @Test
    fun testApiReturnsData() {

        whenever(categoryService.getCategories()).thenReturn(
            Single.create {
                it.onSuccess(Response.success(CategoryDto()))
            }
        )
        categoryApi.getCategories().test()
            .assertNoErrors()
            .assertComplete()

    }

    @Test
    fun testApiReturns401() {
        val responseBody = ResponseBody.create(null, "")
        whenever(categoryService.getCategories()).thenReturn(
            Single.just(Response.error(401, responseBody))
        )
        whenever(converter.convert(any())).thenReturn(ApiErrorResponse("unAuthorized user", 401))
        categoryApi.getCategories()
            .test()
            .assertError(UnAuthorizedException::class.java)
            .assertNoValues()
            .assertNotComplete()

    }
}