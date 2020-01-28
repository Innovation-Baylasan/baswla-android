package org.baylasan.sudanmap.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.domain.event.GetMyEventUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class EventsViewModel(private val getMyEventUseCase: GetMyEventUseCase) : BaseViewModel() {
    private val eventUiEvent = MutableLiveData<UiState<List<Event>>>()
    val eventEvent: LiveData<UiState<List<Event>>> = eventUiEvent
    fun loadMyEvents() {
        eventUiEvent.value = UiState.Loading()
        getMyEventUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                eventUiEvent.value = UiState.Success(it)
            }, {
                eventUiEvent.value = UiState.Error(it)
            }).addToDisposables()
    }
}