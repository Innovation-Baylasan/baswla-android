package org.baylasan.sudanmap.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.R
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.LoginResponse
import org.baylasan.sudanmap.data.user.model.UserDto
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.domain.user.UserLoginUseCase
import org.baylasan.sudanmap.ui.BaseViewModel


class LoginViewModel(
    private val loginUseCase: UserLoginUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel() {

    private val _error = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _error

    val event = MutableLiveData<LoginEvent>()

    //  val errorMessage: String? = _error.value
    fun login(email: String, password: String) {
        validateFields(email, password)
    }

    private fun validateFields(email: String, password: String) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _error.value = R.string.email_not_valid
            return
        }

        if (password.isEmpty()) {
            _error.value = R.string.password_required
            return
        }
        if (password.length < 8) {
            _error.value = R.string.password_should_be_8_chars
            return
        }

        performLogin(email, password)

    }

    private fun performLogin(email: String, password: String) {
        event.value = LoadingEvent
        val request = LoginRequest(email = email, password = password)
        loginUseCase.execute(UserLoginUseCase.Params(request)).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ loginResponse ->
                loginResponse?.let {
                    saveUserSession(it)
                    event.value = DataEvent(it)
                } ?: kotlin.run { EmptyEvent }
            }, {
                event.value = when (it) {
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
                    else -> {
                        ErrorEvent("Unexpected errror")
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