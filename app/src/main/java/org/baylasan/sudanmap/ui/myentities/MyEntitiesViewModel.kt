package org.baylasan.sudanmap.ui.myentities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.entity.GetMyEntitiesUseCase
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.ui.BaseViewModel

class MyEntitiesViewModel(private val useCase: GetMyEntitiesUseCase,private val sessionManager: SessionManager) : BaseViewModel() {
    private val entitiesUiState = MutableLiveData<UiState<List<Entity>>>()
    val entitiesState: LiveData<UiState<List<Entity>>> = entitiesUiState

    fun loadMyEntities() {
        entitiesUiState.value = UiState.Loading()
        useCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                entitiesUiState.value = UiState.Success(it)
            }, {
                if(it is UnAuthorizedException){
                    sessionManager.clear()
                }
                entitiesUiState.value = UiState.Error(it)
            })
            .addToDisposables()
    }
}