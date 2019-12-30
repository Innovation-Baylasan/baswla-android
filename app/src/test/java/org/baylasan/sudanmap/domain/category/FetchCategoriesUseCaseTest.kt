package org.baylasan.sudanmap.domain.category

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Category
import org.junit.Before
import org.junit.Test

class FetchCategoriesUseCaseTest {

    lateinit var repository: CategoryRepository
    lateinit var fetchCategoriesUseCase: FetchCategoriesUseCase
    @Before
    fun setup() {
        repository = mock(verboseLogging = true)
        fetchCategoriesUseCase = FetchCategoriesUseCase(repository)
    }

    @Test
    fun testWhenDataFetchSuccess() {
        whenever(repository.getCategories()).thenReturn(
            Single.just(
                listOf(
                    Category(),
                    Category(),
                    Category()
                )
            )
        )
        fetchCategoriesUseCase.execute()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                it.size == 3
            }
    }

    @Test
    fun testWhenDataFetchIsEmpty() {
        whenever(repository.getCategories()).thenReturn(Single.just(listOf()))

        fetchCategoriesUseCase.execute()
            .test()
            .assertValue { it.isEmpty() }

    }

    @Test
    fun testWhenDataFetchFails() {
        whenever(repository.getCategories()).thenReturn(Single.error(Exception()))
        fetchCategoriesUseCase.execute()
            .test()
            .assertError(Exception::class.java)
            .assertNoValues()
            .assertNotComplete()


    }
}