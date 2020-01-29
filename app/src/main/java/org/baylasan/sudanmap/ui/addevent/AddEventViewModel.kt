package org.baylasan.sudanmap.ui.addevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.event.model.AddEventRequest
import org.baylasan.sudanmap.domain.event.AddEventUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class AddEventViewModel(private val addEventUseCase: AddEventUseCase) : BaseViewModel() {
    private val addUiState = MutableLiveData<UiState<Unit>>()
    val addState: LiveData<UiState<Unit>> = addUiState
    fun submitEvent(eventRequest: AddEventRequest) {
        addUiState.value = UiState.Loading()
        addEventUseCase.execute(AddEventUseCase.Request(eventRequest))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                addUiState.value = UiState.Complete()

            }, {
                addUiState.value = UiState.Error(it)

            })
            .addToDisposables()
    }
}