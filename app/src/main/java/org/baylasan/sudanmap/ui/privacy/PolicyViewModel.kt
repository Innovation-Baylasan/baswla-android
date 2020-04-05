package org.baylasan.sudanmap.ui.privacy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.domain.policy.GetPolicyUseCase
import org.baylasan.sudanmap.domain.policy.model.Policy
import org.baylasan.sudanmap.ui.BaseViewModel

class PolicyViewModel(private val usecase: GetPolicyUseCase) : BaseViewModel() {
    private val state = MutableLiveData<UiState<Policy>>()
    val uiState: LiveData<UiState<Policy>> = state

    fun loadPolicy() {
        usecase.execute()
            .compose {
                state.value = UiState.Loading()
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                state.value = UiState.Success(it)
            }, {
                state.value = UiState.Error(it)
            })
            .addToDisposables()
    }
}