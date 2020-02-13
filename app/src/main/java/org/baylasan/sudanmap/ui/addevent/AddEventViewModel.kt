package org.baylasan.sudanmap.ui.addevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.domain.event.AddEventUseCase
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.ui.BaseViewModel

class AddEventViewModel(private val addEventUseCase: AddEventUseCase,private val sessionManager: SessionManager) : BaseViewModel() {
    private val addUiState = MutableLiveData<UiState<Event>>()
    val addState: LiveData<UiState<Event>> = addUiState
    fun submitEvent(eventRequest: AddEventRequest) {
        addUiState.value = UiState.Loading()
        addEventUseCase.execute(AddEventUseCase.Request(eventRequest))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                addUiState.value = UiState.Success(it)

            }, {
                if(it is UnAuthorizedException){
                    sessionManager.clear()
                }
                addUiState.value = UiState.Error(it)

            })
            .addToDisposables()
    }
}