package org.baylasan.sudanmap.ui.auth.company

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.entity.model.EntityDetails
import org.baylasan.sudanmap.data.user.model.AuthenticationResponse
import org.baylasan.sudanmap.data.user.model.Data
import org.baylasan.sudanmap.data.user.model.RegisterCompanyRequest
import org.baylasan.sudanmap.data.user.model.User
import org.baylasan.sudanmap.domain.user.CompanyRegisterUseCase
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.ui.auth.signup.DataEvent
import org.baylasan.sudanmap.ui.auth.signup.RegisterEvent
import org.junit.Rule
import org.junit.Test

class CompleteRegisterViewModelTest {
    private val sessionManager = mock<SessionManager>(verboseLogging = true)
    private val registerCompanyObserver = mock<Observer<RegisterEvent>>()
    private val registerUseCase = mock<CompanyRegisterUseCase>()
    private val registerViewMode by lazy {
        CompleteRegisterViewModel(
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
    fun `test company register happy case`() {
        whenever(registerUseCase.execute(any())).thenReturn(
            Single.just(
                AuthenticationResponse(
                    Data(
                        "",
                        User(),
                        EntityDetails()
                    )
                )
            )
        )
        registerViewMode.registerCompany(
            RegisterCompanyRequest(
                "",
                "",
                "",
                "",
                "",
                1,
                null,
                null,
                ""
            )
        )
        registerViewMode.events.observeForever(registerCompanyObserver)

        argumentCaptor<RegisterEvent> {
            verify(registerCompanyObserver).onChanged(capture())
            assert(lastValue is DataEvent)
            print(allValues)
            verify(sessionManager).saveEntity(any())
            verify(sessionManager).saveUserSession(any())
            verifyNoMoreInteractions(registerCompanyObserver)
            verifyNoMoreInteractions(sessionManager)
        }
    }
}