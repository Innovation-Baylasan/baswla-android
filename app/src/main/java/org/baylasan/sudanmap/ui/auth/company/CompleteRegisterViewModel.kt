package org.baylasan.sudanmap.ui.auth.company

import androidx.lifecycle.MutableLiveData
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.user.model.RegisterCompanyRequest
import org.baylasan.sudanmap.data.user.model.AuthenticationResponse
import org.baylasan.sudanmap.data.user.model.UserDto
import org.baylasan.sudanmap.data.user.model.stringify
import org.baylasan.sudanmap.domain.user.CompanyRegisterUseCase
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.ui.BaseViewModel
import org.baylasan.sudanmap.ui.auth.signup.*

class CompleteRegisterViewModel(
    private val companyRegisterUseCase: CompanyRegisterUseCase,
    private val sessionManager: SessionManager,
    private val ioScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : BaseViewModel() {
    val events = MutableLiveData<RegisterEvent>()
    fun registerCompany(registerCompanyRequest: RegisterCompanyRequest) {
        events.value = LoadingEvent

        companyRegisterUseCase.execute(CompanyRegisterUseCase.Params(registerCompanyRequest))
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribe({
                events.value = DataEvent
                saveUserSession(it)
            },{
                events.value = when (it) {
                    is TimeoutConnectionException -> {
                        TimeoutEvent
                    }
                    is ConnectionException,
                    is ClientConnectionException -> {
                        NetworkErrorEvent
                    }
                    is ApiException -> {
                        ErrorEvent(it.apiErrorResponse.message)
                    }

                    is ResponseException -> {
                        ErrorEvent(it.message)
                    }
                    is RegistrationResponseException -> {
                        val stringify = it.registrationResponse.errors.stringify()
                        ErrorEvent(stringify)
                    }
                    else -> ErrorEvent("Unknown Error")
                }

            })
            .addToDisposables()

    }

    private fun onError(): (Throwable) -> Unit {
        return {
        }
    }



    private fun saveUserSession(response: AuthenticationResponse) {
        val user = response.data.user
        val userDto = UserDto(
            email = user.email,
            name = user.name,
            token = response.data.token,
            id = user.id,
            username = user.username,
            role = user.role

        )
        val entityDetails = response.data.entity
        if(entityDetails!=null)
        sessionManager.saveEntity(entityDetails)
        sessionManager.saveUserSession(userDto)
    }

}