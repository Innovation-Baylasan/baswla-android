package org.baylasan.sudanmap.ui.main.event

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.event.model.EventResponse
import org.baylasan.sudanmap.domain.event.GetEventUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class EventViewModel(private val useCase: GetEventUseCase) : BaseViewModel() {
    val dataLiveData = MutableLiveData<EventResponse>()
    val errorLiveData = MutableLiveData<Throwable>()
    fun loadEvents() {
        useCase.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                dataLiveData.value = it

            }, {
                errorLiveData.value = it
            })
            .addToDisposables()
    }
}