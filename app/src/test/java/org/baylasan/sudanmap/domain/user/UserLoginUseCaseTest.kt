package org.baylasan.sudanmap.domain.user

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.baylasan.sudanmap.data.user.model.AuthenticationResponse
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.LoginResponse
import org.junit.Before
import org.junit.Test

class UserLoginUseCaseTest {
    private lateinit var repository: UserRepository
    private lateinit var useCase: UserLoginUseCase

    @Before
    fun setup() {
        repository = mock(verboseLogging = true)
        useCase = UserLoginUseCase(repository)
    }

    @Test
    fun `test login happy case`() {
        whenever(repository.login(any())).thenReturn(Single.just(AuthenticationResponse()))
        useCase.execute(UserLoginUseCase.Params(LoginRequest("mail@domain.com", "000000")))
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `test login empty password`() {
        whenever(repository.login(any())).thenReturn(Single.error(EmptyFieldException()))
        useCase.execute(UserLoginUseCase.Params(LoginRequest("mail@domain.com", "")))
            .test()
            .assertError(EmptyFieldException::class.java)
            .assertNoValues()
            .assertNotComplete()
    }

    @Test
    fun `test login empty email`() {
        whenever(repository.login(any())).thenReturn(Single.error(EmptyFieldException()))
        useCase.execute(UserLoginUseCase.Params(LoginRequest("", "00000")))
            .test()
            .assertError(EmptyFieldException::class.java)
            .assertNoValues()
            .assertNotComplete()
    }
    @Test
    fun `test login invalid email`() {
        whenever(repository.login(any())).thenReturn(Single.error(EmptyFieldException()))
        useCase.execute(UserLoginUseCase.Params(LoginRequest("mega", "00000")))
            .test()
            .assertError(InvalidEmailAddressException::class.java)
            .assertNoValues()
            .assertNotComplete()
    }
}