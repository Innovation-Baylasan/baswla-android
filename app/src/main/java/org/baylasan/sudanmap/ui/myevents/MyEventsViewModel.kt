package org.baylasan.sudanmap.ui.myevents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Scheduler
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.domain.event.DeleteEventUseCase
import org.baylasan.sudanmap.domain.event.GetMyEventUseCase
import org.baylasan.sudanmap.domain.event.Request
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.ui.BaseViewModel

class MyEventsViewModel(
    private val getMyEventUseCase: GetMyEventUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val sessionManager: SessionManager,
    private val ioSchedulers: Scheduler,
    private val mainSchedulers: Scheduler
) : BaseViewModel() {
    private val eventUiEvent = MutableLiveData<UiState<List<Event>>>()
    private val deleteEventUiEvent = MutableLiveData<UiState<Int>>()
    val eventEvent: LiveData<UiState<List<Event>>> = eventUiEvent
    val deleteEventEvent: LiveData<UiState<Int>> = deleteEventUiEvent
    fun loadMyEvents() {
        eventUiEvent.value = UiState.Loading()
        getMyEventUseCase.execute()
            .subscribeOn(ioSchedulers)
            .observeOn(mainSchedulers)
            .subscribe({
                eventUiEvent.value = UiState.Success(it)
            }, {
                if (it is UnAuthorizedException) {
                    sessionManager.clear()
                }
                eventUiEvent.value = UiState.Error(it)
            }).addToDisposables()
    }

    fun deleteEvent(id: Int, position: Int) {
        deleteEventUseCase.execute(Request(id))
            .compose {
                deleteEventUiEvent.value = UiState.Loading()
                it
            }
            .subscribeOn(ioSchedulers)
            .observeOn(mainSchedulers)
            .subscribe({
                deleteEventUiEvent.value = UiState.Success(position)

            }, {
                if (it is UnAuthorizedException) {
                    sessionManager.clear()
                }
                deleteEventUiEvent.value = UiState.Error(it)
            })
            .addToDisposables()
    }
}