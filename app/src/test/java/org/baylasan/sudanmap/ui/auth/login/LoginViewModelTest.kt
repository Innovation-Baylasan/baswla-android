package org.baylasan.sudanmap.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.entity.model.EntityDetails
import org.baylasan.sudanmap.data.user.model.AuthenticationResponse
import org.baylasan.sudanmap.data.user.model.Data
import org.baylasan.sudanmap.domain.user.EmptyFieldException
import org.baylasan.sudanmap.domain.user.InvalidEmailAddressException
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.domain.user.UserLoginUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class LoginViewModelTest {
    private val sessionManager: SessionManager = mock(verboseLogging = true)
    private val userLoginUseCase: UserLoginUseCase = mock(verboseLogging = true)
    private val observer = mock<Observer<UiState<Unit>>>()
    private val loginViewModel: LoginViewModel by lazy {
        LoginViewModel(
            userLoginUseCase,
            sessionManager,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    @Before
    fun setup() {
        reset(userLoginUseCase, observer)
    }


    @Test
    fun `test when company user login happy case`() {
        whenever(userLoginUseCase.execute(any())).thenReturn(
            Single.just(
                AuthenticationResponse(
                    data = Data(
                        entity = EntityDetails()
                    )
                )
            )
        )
        loginViewModel.login("email@mail.com", "12345678")
        loginViewModel.event.observeForever(observer)
        verify(userLoginUseCase).execute(any())

        argumentCaptor<UiState<Unit>> {
            verify(observer, times(1)).onChanged(capture())
            assert(lastValue is UiState.Complete)
            verify(sessionManager).saveUserSession(any())
            verify(sessionManager).saveEntity(any())
            verifyNoMoreInteractions(observer)
            verifyNoMoreInteractions(sessionManager)
        }
    }

    @Test
    fun `test when  user login happy case`() {
        whenever(userLoginUseCase.execute(any())).thenReturn(
            Single.just(AuthenticationResponse())
        )
        loginViewModel.login("email@mail.com", "12345678")
        loginViewModel.event.observeForever(observer)
        verify(userLoginUseCase).execute(any())

        argumentCaptor<UiState<Unit>> {
            verify(observer, times(1)).onChanged(capture())
            assert(lastValue is UiState.Complete)
            verify(sessionManager).saveUserSession(any())
            verifyNoMoreInteractions(observer)
            verifyNoMoreInteractions(sessionManager)


        }

    }

    @Test
    fun `test when  user login invalid email`() {
        whenever(userLoginUseCase.execute(any())).thenReturn(
            Single.error(InvalidEmailAddressException())
        )
        loginViewModel.login("emailmail.com", "12345678")
        loginViewModel.event.observeForever(observer)
        verify(userLoginUseCase).execute(any())

        argumentCaptor<UiState<Unit>> {
            verify(observer, times(1)).onChanged(capture())
            assert(lastValue is UiState.Error)
            val error = lastValue as UiState.Error
            assert(error.throwable is InvalidEmailAddressException)
            verifyNoMoreInteractions(observer)
            verifyNoMoreInteractions(sessionManager)
        }
    }

    @Test
    fun `test when user login empty password`() {
        whenever(userLoginUseCase.execute(any())).thenReturn(
            Single.error(EmptyFieldException())
        )
        loginViewModel.login("email@mail.com", "")
        loginViewModel.event.observeForever(observer)
        verify(userLoginUseCase).execute(any())

        argumentCaptor<UiState<Unit>> {
            verify(observer).onChanged(capture())
            assert(lastValue is UiState.Error)
            val error = lastValue as UiState.Error
            assert(error.throwable is EmptyFieldException)
            verifyNoMoreInteractions(observer)
        }
    }

    @Test
    fun `test when user login both email and password are empty`() {
        whenever(userLoginUseCase.execute(any())).thenReturn(
            Single.error(EmptyFieldException())
        )
        loginViewModel.login("", "")
        loginViewModel.event.observeForever(observer)
        verify(userLoginUseCase).execute(any())

        argumentCaptor<UiState<Unit>> {
            verify(observer, times(1)).onChanged(capture())
            assert(lastValue is UiState.Error)
            val error = lastValue as UiState.Error
            assert(error.throwable is EmptyFieldException)
            verifyNoMoreInteractions(observer)
            verifyNoMoreInteractions(sessionManager)
        }
    }


}