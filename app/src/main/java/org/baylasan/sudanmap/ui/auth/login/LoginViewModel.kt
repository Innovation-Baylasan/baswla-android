package org.baylasan.sudanmap.ui.auth.login

import androidx.lifecycle.MutableLiveData
import io.reactivex.Scheduler
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.user.model.AuthenticationResponse
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.UserDto
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.domain.user.UserLoginUseCase
import org.baylasan.sudanmap.ui.BaseViewModel


class LoginViewModel(
    private val loginUseCase: UserLoginUseCase,
    private val sessionManager: SessionManager,
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler

) : BaseViewModel() {


    val event = MutableLiveData<UiState<Unit>>()

    fun login(email: String, password: String) {
        performLogin(email, password)
    }


    private fun performLogin(email: String, password: String) {
        val request = LoginRequest(email = email, password = password)

        loginUseCase.execute(UserLoginUseCase.Params(request))
            .compose {
                event.value = UiState.Loading()
                it
            }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe({ loginResponse ->
                event.value = UiState.Complete()
                saveUserSession(loginResponse)
            }, {
                event.value = UiState.Error(it)
            }).addToDisposables()
    }

    private fun saveUserSession(response: AuthenticationResponse) {

        val user = response.data.user
        val entity = response.data.entity
        if (entity != null) {
            sessionManager.saveEntity(entity)
        }
        val userDto = UserDto(
            email = user.email,
            name = user.name,
            token = response.data.token,
            id = user.id,
            role = user.role
        )
        sessionManager.saveUserSession(userDto)
    }
}