package org.baylasan.sudanmap.ui.eventsearch

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.domain.event.FindEventUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class EventSearchViewModel(private val findEventUseCase: FindEventUseCase) : BaseViewModel() {
    val events = MutableLiveData<UiState<List<Event>>>()


    fun findEvents(key: String) {
        findEventUseCase.execute(FindEventUseCase.Request(key))
            .compose {
                events.value = UiState.Loading()
                it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    events.value = UiState.Empty()
                } else {
                    events.value =
                        UiState.Success(it)
                }
            }, {
                events.value = UiState.Error(it)
            }).addToDisposables()


    }
}