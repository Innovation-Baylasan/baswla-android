package org.baylasan.sudanmap.ui.auth.signup

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.user.model.*
import org.baylasan.sudanmap.domain.user.CompanyRegisterUseCase
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.domain.user.UserRegisterUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class RegisterViewModel(
    private val registerUseCase: UserRegisterUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel() {
    val moveToCompleteRegister = MutableLiveData<RegisterRequest>()
    val events = MutableLiveData<RegisterEvent>()



    fun register(registerRequest: RegisterRequest) {
        if (registerRequest.type == "user") {
            events.value = LoadingEvent
            registerUseCase.execute(UserRegisterUseCase.Params(registerRequest))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                is RegisterationResponseExcetpion -> {
                    val stringify = it.registerationResponse.errors.stringify()
                    ErrorEvent(stringify)
                }
                else -> ErrorEvent("Unknown Error")
            }
        }
    }

    private fun onSuccess(): (RegisterResponse) -> Unit {
        return { registerResponse: RegisterResponse ->
            saveUserSession(registerResponse)
            events.value = DataEvent(registerResponse)
        }
    }

    private fun saveUserSession(response: RegisterResponse) {
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