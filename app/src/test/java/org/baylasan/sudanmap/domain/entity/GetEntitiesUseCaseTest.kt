package org.baylasan.sudanmap.domain.entity

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.baylasan.sudanmap.data.common.ApiErrorResponse
import org.baylasan.sudanmap.data.common.ApiException
import org.baylasan.sudanmap.data.entity.model.Entity
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetEntitiesUseCaseTest {

    private lateinit var repository: EntityRepository
    private lateinit var getEntitiesUseCase: GetEntitiesUseCase


    @Before
    fun setUp() {
        repository = mock(verboseLogging = true)
        getEntitiesUseCase = GetEntitiesUseCase(repository)
    }

    @Test
    fun testWhenFetchDataSuccess() {
        whenever(repository.getEntities()).thenReturn(
            Single.just(
              listOf(
                  Entity(),
                  Entity(),
                  Entity()
              ))
            )

        getEntitiesUseCase.execute()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                it.size == 3
            }
    }


    @Test
    fun testWhenDataFetchIsEmpty() {
        whenever(repository.getEntities()).thenReturn(Single.just( listOf()))

        getEntitiesUseCase.execute()
            .test()
            .assertValue { it.isEmpty() }
    }


    @Test
    fun testWhenFetchFails() {
        whenever(repository.getEntities()).thenReturn(
            Single.error(
                Exception()
            )
        )

        getEntitiesUseCase.execute()
            .test()
            .assertError(Exception::class.java)
            .assertNoValues()
            .assertNotComplete()
    }

    @Test
    fun testWhenFetchFailsWithNotAuthenticated() {
        whenever(repository.getEntities()).thenReturn(
            Single.error(
                ApiException(
                    ApiErrorResponse(
                        "",
                        401
                    )
                )              // HttpException(Response.error(401, ResponseBody.create(MediaType.parse("application/json") , "error")))
            )
        )

        getEntitiesUseCase.execute()
            .test()
            .assertError(ApiException::class.java)
            .assertNoValues()
            .assertNotComplete()
    }

    @After
    fun tearDown() {
    }
}