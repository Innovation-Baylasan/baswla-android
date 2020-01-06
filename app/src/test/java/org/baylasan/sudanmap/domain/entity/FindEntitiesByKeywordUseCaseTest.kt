package org.baylasan.sudanmap.domain.entity

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Entity
import org.junit.Before
import org.junit.Test

class FindEntitiesByKeywordUseCaseTest {
    private lateinit var repository: EntityRepository
    private lateinit var useCase: FindEntitiesByKeywordUseCase
    @Before
    fun setUp() {
        repository = mock(verboseLogging = true)
        useCase = FindEntitiesByKeywordUseCase(repository)
    }

    @Test
    fun `test happy case`() {
        whenever(repository.findEntitiesByKeyword(any())).thenReturn(
            Single.just(
                listOf(
                    Entity(),
                    Entity()
                )
            )
        )

        useCase.execute(FindEntitiesByKeywordUseCase.Request("any"))
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun `test when keyword is empty`() {
        whenever(repository.findEntitiesByKeyword(any())).thenReturn(
            Single.just(
                listOf(
                    Entity(),
                    Entity()
                )
            )
        )
        useCase.execute(FindEntitiesByKeywordUseCase.Request(""))
            .test()
            .assertNotComplete()
            .assertNoValues()
            .assertError(KeywordEmptyException::class.java)
    }
}