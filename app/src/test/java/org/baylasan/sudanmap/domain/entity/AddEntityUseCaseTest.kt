package org.baylasan.sudanmap.domain.entity

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import org.baylasan.sudanmap.data.entity.model.AddEntityRequest
import org.baylasan.sudanmap.mockLogging
import org.junit.Before
import org.junit.Test

class AddEntityUseCaseTest {
    private lateinit var repository: EntityRepository
    private lateinit var addEntityUseCase: AddEntityUseCase
    @Before
    fun setup() {
        repository = mockLogging()
        addEntityUseCase = AddEntityUseCase(repository)
    }
    @Test
    fun `add entity happy case`() {
        whenever(repository.addEntity(any()))
            .thenReturn(Completable.complete())
        addEntityUseCase.execute(AddEntityUseCase.Request(AddEntityRequest()))
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertNoValues()
    }
    @Test
    fun `add entity bad case`(){
        val throwable = Throwable()
        whenever(repository.addEntity(any()))
            .thenReturn(Completable.error(throwable))
        addEntityUseCase.execute(AddEntityUseCase.Request(AddEntityRequest()))
            .test()
            .assertError(throwable)
            .assertNotComplete()
            .assertNoValues()
    }
}