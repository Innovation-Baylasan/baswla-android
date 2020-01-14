package org.baylasan.sudanmap.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.domain.user.UserRegisterUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class RegisterViewModel(private val registerUseCase: UserRegisterUseCase) : BaseViewModel() {
    val events = MutableLiveData<RegisterEvent>()

    private val _error = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _error

    fun register(registerRequest: RegisterRequest) {
        events.value = LoadingEvent
        registerUseCase.execute(UserRegisterUseCase.Params(registerRequest)).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it?.let {
                    DataEvent(it)
                } ?: kotlin.run { EmptyEvent }
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
}