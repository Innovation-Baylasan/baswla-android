package org.baylasan.sudanmap.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.data.user.model.RegisterResponse
import org.baylasan.sudanmap.data.user.model.UserDto
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.domain.user.UserRegisterUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class RegisterViewModel(
    private val registerUseCase: UserRegisterUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel() {
    val events = MutableLiveData<RegisterEvent>()

    private val _error = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _error

    fun register(registerRequest: RegisterRequest) {
        events.value = LoadingEvent
        registerUseCase.execute(UserRegisterUseCase.Params(registerRequest)).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ registerResponse ->
                saveUserSession(registerResponse)
                events.value = DataEvent(registerResponse)
            }, {
                events.value = when (it) {
                    is UnAuthorizedException -> {
                        SessionExpiredEvent
                    }
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
                    else -> {
                        ErrorEvent("Unexpected errror")
                    }
                }
            }).addToDisposables()
    }

    private fun saveUserSession(response: RegisterResponse) {
        val user = response.data.user
        val userDto = UserDto(
            email = user.email,
            name = user.name,
            token = response.data.token,
            id = user.id,
            username = user.username
        )
        sessionManager.saveUserSession(userDto)
    }
}