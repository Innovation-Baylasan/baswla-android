package org.baylasan.sudanmap.ui.auth.signup

import androidx.lifecycle.MutableLiveData
import io.reactivex.Scheduler
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.user.model.AuthenticationResponse
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.data.user.model.UserDto
import org.baylasan.sudanmap.data.user.model.stringify
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.domain.user.UserRegisterUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class RegisterViewModel(
    private val registerUseCase: UserRegisterUseCase,
    private val sessionManager: SessionManager,
    private val ioScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : BaseViewModel() {
    val moveToCompleteRegister = MutableLiveData<RegisterRequest>()
    val events = MutableLiveData<RegisterEvent>()


    fun register(registerRequest: RegisterRequest) {
        if (registerRequest.type == "user") {
            registerUseCase.execute(UserRegisterUseCase.Params(registerRequest))
                .compose {
                    events.value = LoadingEvent
                    it
                }
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(onSuccess(), onError())
                .addToDisposables()
        } else {
            moveToCompleteRegister.value = registerRequest
        }
    }

    private fun onError(): (Throwable) -> Unit {
        return {
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
        }
    }

    private fun onSuccess(): (AuthenticationResponse) -> Unit {
        return { registerResponse: AuthenticationResponse ->
            saveUserSession(registerResponse)
            events.value = DataEvent
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
        sessionManager.saveUserSession(userDto)
    }
}