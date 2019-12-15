package org.baylasan.sudanmap.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.domain.entity.GetEntitiesUseCase
import org.baylasan.sudanmap.domain.entity.model.EntityResponseDto
import org.baylasan.sudanmap.ui.BaseViewModel
import org.baylasan.sudanmap.ui.layers.*

class EntityViewModel(private val getEntitiesUseCase: GetEntitiesUseCase) : BaseViewModel() {

    private val _entity = MutableLiveData<EntityResponseDto>()

    val entities :LiveData<EntityResponseDto> = _entity
    val events = MutableLiveData<AppEvent>()


    @SuppressLint("CheckResult")
    fun loadEntity() {
        events.value = LoadingEvent

        getEntitiesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                events.value = if (it.data.isNotEmpty()) {
                    DataEvent(it)
                } else {
                    EmptyEvent
                }
             /*   _entity.postValue(it)
                Log.d("MEGA", it.toString())*/
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
                    else -> {
                        ErrorEvent("Unexpected errror")
                    }
                }
            }).addToDisposables()
    }

}