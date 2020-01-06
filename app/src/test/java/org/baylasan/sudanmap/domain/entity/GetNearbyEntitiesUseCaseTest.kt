package org.baylasan.sudanmap.domain.entity

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Entity
import org.junit.Before
import org.junit.Test

class GetNearbyEntitiesUseCaseTest {
    private lateinit var repository: EntityRepository
    private lateinit var useCase: GetNearbyEntitiesUseCase
    @Before
    fun setUp() {
        repository = mock(verboseLogging = true)
        useCase = GetNearbyEntitiesUseCase(repository)
    }

    @Test
    fun `get nearby entities happy case`() {
        whenever(repository.getNearbyEntities(any(), any()))
            .thenReturn(Single.just(listOf(Entity(), Entity())))

        useCase.execute(GetNearbyEntitiesUseCase.Params(12.0, 32.0))
            .test()
            .assertNoErrors()
            .assertValueCount(1)
            .assertComplete()
    }

}