package org.baylasan.sudanmap.ui.auth.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.user.model.*
import org.baylasan.sudanmap.domain.user.CompanyRegisterUseCase
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.domain.user.UserRegisterUseCase
import org.baylasan.sudanmap.ui.BaseViewModel
import java.io.File

class RegisterViewModel(
    private val registerUseCase: UserRegisterUseCase,
    private val companyRegisterUseCase: CompanyRegisterUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel() {
    val events = MutableLiveData<RegisterEvent>()
    val moveToCompleteRegister = MutableLiveData<RegisterRequest>()
    fun registerCompany(){
        companyRegisterUseCase.execute(CompanyRegisterUseCase.Params( RegisterCompanyRequest(
            "mail@domain.com",
            "meg",
            "00000000",
            "00000000",
            1,
            "",
            1,
            File(""),
            File(""),
            "lalalalal"

        )
        )).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            },{it.printStackTrace()})
    }
    fun register(registerRequest: RegisterRequest) {
        if (registerRequest.type == 9) {
            events.value = LoadingEvent
            registerUseCase.execute(UserRegisterUseCase.Params(registerRequest))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ registerResponse ->
                    saveUserSession(registerResponse)
                    events.value = DataEvent(registerResponse)
                }, {
                    Log.d("MEGA", "ERROR ${it.javaClass.simpleName}")
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
                }).addToDisposables()
        } else {
            moveToCompleteRegister.value = registerRequest
        }
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