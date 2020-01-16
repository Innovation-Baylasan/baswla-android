package org.baylasan.sudanmap.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.common.ApiException
import org.baylasan.sudanmap.data.common.ClientConnectionException
import org.baylasan.sudanmap.data.common.ConnectionException
import org.baylasan.sudanmap.data.common.TimeoutConnectionException
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.LoginResponse
import org.baylasan.sudanmap.data.user.model.UserDto
import org.baylasan.sudanmap.domain.user.EmptyFieldException
import org.baylasan.sudanmap.domain.user.InvalidEmailAddressException
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.domain.user.UserLoginUseCase
import org.baylasan.sudanmap.ui.BaseViewModel


class LoginViewModel(
    private val loginUseCase: UserLoginUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel() {


    val event = MutableLiveData<LoginEvent>()

    fun login(email: String, password: String) {
        performLogin(email, password)
    }


    private fun performLogin(email: String, password: String) {
        val request = LoginRequest(email = email, password = password)
        event.value = LoadingEvent

        loginUseCase.execute(UserLoginUseCase.Params(request))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ loginResponse ->
                saveUserSession(loginResponse)
                event.value = DataEvent
            }, {
                Log.d("MEGA",it.toString())
                event.value = when (it) {
                    is InvalidEmailAddressException -> {
                        ValidationErrorEvent(R.string.email_not_valid)
                    }
                    is EmptyFieldException -> {
                        ValidationErrorEvent(0)
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
                    else -> {
                        ErrorEvent("Unexpected error")
                    }
                }
            }).addToDisposables()
    }

    private fun saveUserSession(response: LoginResponse) {
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