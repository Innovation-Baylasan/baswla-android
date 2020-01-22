package org.baylasan.sudanmap.ui.main.event

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.event.model.Event
import org.baylasan.sudanmap.domain.event.GetEventUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class EventViewModel(private val useCase: GetEventUseCase) : BaseViewModel() {
    private val uiState = MutableLiveData<UiState<List<Event>>>()

    fun uiState() = uiState
    fun loadEvents() {
        uiState.value = UiState.Loading()
        useCase.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                uiState.value = UiState.Success(it.events)
            }, {
                uiState.value = UiState.Error(it)
            }).addToDisposables()
    }
}