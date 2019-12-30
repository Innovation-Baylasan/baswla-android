package org.baylasan.sudanmap.data.entity

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import okhttp3.ResponseBody
import org.baylasan.sudanmap.data.SudanMapApi
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import retrofit2.Response
import java.net.SocketTimeoutException

class EntityApiTest {


    private lateinit var entityApi: EntityApi
    private lateinit var entityService: SudanMapApi.Entities
    private lateinit var converter: Converter<ResponseBody, ApiErrorResponse>

    @Before
    fun setUp() {
        entityService = mock(verboseLogging = true)
        converter = mock(verboseLogging = true)
        entityApi = EntityApi(entityApi = entityService, errorConverter = converter)
    }


    @Test
    fun assertApiReturnData() {
        whenever(entityService.getEntities()).thenReturn(Single.create {
            it.onSuccess(
                Response.success(
                   listOf()
                )
            )
        })

        entityApi.getEntities().test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testApiReturns401() {
        val responseBody = ResponseBody.create(null, "")
        whenever(entityService.getEntities()).thenReturn(
            Single.just(Response.error(401, responseBody))
        )

        whenever(converter.convert(any())).thenReturn(ApiErrorResponse("unAuthorized user", 401))
        entityApi.getEntities()
            .test()
            .assertError(UnAuthorizedException::class.java)
            .assertNoValues()
            .assertNotComplete()

    }


    @Test
    fun testApiReturnTimeOut() {
        whenever(entityService.getEntities()).thenReturn(Single.create {
            it.onError(SocketTimeoutException())
        })

        entityApi.getEntities()
            .test()
            .assertError(SocketTimeoutException::class.java)
            .assertNoValues()
            .assertNotComplete()
    }
}