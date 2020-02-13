package org.baylasan.sudanmap.ui.addentity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.entity.model.AddEntityRequest
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.entity.AddEntityUseCase
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.ui.BaseViewModel

class AddEntityViewModel(
    private val useCase: AddEntityUseCase,
    private val sessionManager: SessionManager
) : BaseViewModel() {
    private val addUiState = MutableLiveData<UiState<Entity>>()
    val addState: LiveData<UiState<Entity>> = addUiState
    fun add(addEntityRequest: AddEntityRequest) {
        addUiState.value = UiState.Loading()
        useCase.execute(AddEntityUseCase.Request(addEntityRequest))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                addUiState.value = UiState.Success(it)
            }, {
                if (it is UnAuthorizedException) {
                    sessionManager.clear()
                }
                addUiState.value = UiState.Error(it)
            })
            .addToDisposables()
    }
}