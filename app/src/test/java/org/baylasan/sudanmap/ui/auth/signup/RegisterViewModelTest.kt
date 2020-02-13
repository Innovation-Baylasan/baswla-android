package org.baylasan.sudanmap.ui.auth.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.ConnectionException
import org.baylasan.sudanmap.data.common.RegistrationResponseException
import org.baylasan.sudanmap.data.common.TimeoutConnectionException
import org.baylasan.sudanmap.data.user.model.AuthenticationResponse
import org.baylasan.sudanmap.data.user.model.Errors
import org.baylasan.sudanmap.data.user.model.RegisterErrorResponse
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.domain.user.UserRegisterUseCase
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {
    private val sessionManager = mock<SessionManager>(verboseLogging = true)
    private val observer = mock<Observer<RegisterEvent>>()
    private val registerCompanyObserver = mock<Observer<RegisterRequest>>()
    private val registerUseCase = mock<UserRegisterUseCase>()
    private val registerViewModel: RegisterViewModel by lazy {
        RegisterViewModel(
            registerUseCase,
            sessionManager,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }
    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    @Test
    fun `test user register happy case`() {
        whenever(registerUseCase.execute(any())).thenReturn(Single.just(AuthenticationResponse()))
        registerViewModel.register(RegisterRequest())
        registerViewModel.events.observeForever(observer)

        argumentCaptor<RegisterEvent> {
            verify(observer).onChanged(capture())
            assert(lastValue is DataEvent)
            verify(sessionManager).saveUserSession(any())
            verifyZeroInteractions(sessionManager)
            print(allValues)
        }
    }

    @Test
    fun `test user select company register happy case`() {
        whenever(registerUseCase.execute(any())).thenReturn(Single.just(AuthenticationResponse()))
        val request = RegisterRequest(type = "company")
        registerViewModel.register(request)
        registerViewModel.events.observeForever(observer)
        registerViewModel.moveToCompleteRegister.observeForever(registerCompanyObserver)

        argumentCaptor<RegisterRequest> {
            verify(registerCompanyObserver).onChanged(capture())
            verifyZeroInteractions(observer)
            verifyZeroInteractions(sessionManager)
            assert(lastValue == request)
        }
    }

    @Test
    fun `test user register when request timed out `() {
        whenever(registerUseCase.execute(any())).thenReturn(
            Single.error(
                TimeoutConnectionException(
                    "timed out"
                )
            )
        )
        registerViewModel.register(RegisterRequest())
        registerViewModel.events.observeForever(observer)

        argumentCaptor<RegisterEvent> {
            verify(observer).onChanged(capture())
            assert(lastValue is TimeoutEvent)
            verifyZeroInteractions(sessionManager)
        }
    }

    @Test
    fun `test user register when no active network `() {
        whenever(registerUseCase.execute(any())).thenReturn(
            Single.error(
                ConnectionException(
                    "timed out"
                )
            )
        )
        registerViewModel.register(RegisterRequest())
        registerViewModel.events.observeForever(observer)

        argumentCaptor<RegisterEvent> {
            verify(observer).onChanged(capture())
            assert(lastValue is NetworkErrorEvent)
            verifyZeroInteractions(sessionManager)
        }
    }

    @Test
    fun `test user register when one required field not sent`() {
        whenever(registerUseCase.execute(any())).thenReturn(
            Single.error(
                RegistrationResponseException(
                    RegisterErrorResponse(
                        errors = Errors(
                            listOf("email is required"),
                            listOf("password is required"),
                            listOf("username is taken")
                        ), httpCode = 404
                    )
                )
            )
        )
        registerViewModel.register(RegisterRequest(name = "user"))
        registerViewModel.events.observeForever(observer)

        argumentCaptor<RegisterEvent> {
            verify(observer).onChanged(capture())
            assert(lastValue is ErrorEvent)
            verifyZeroInteractions(sessionManager)
            print((allValues.first() as ErrorEvent).errorMessage)
        }
    }


}