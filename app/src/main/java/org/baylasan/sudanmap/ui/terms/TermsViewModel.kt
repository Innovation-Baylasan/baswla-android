package org.baylasan.sudanmap.ui.terms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.domain.policy.GetPolicyUseCase
import org.baylasan.sudanmap.domain.policy.model.Policy
import org.baylasan.sudanmap.domain.terms.GetTermsUseCase
import org.baylasan.sudanmap.domain.terms.model.Terms
import org.baylasan.sudanmap.ui.BaseViewModel

class TermsViewModel(private val usecase: GetTermsUseCase) : BaseViewModel() {
    private val state = MutableLiveData<UiState<Terms>>()
    val uiState: LiveData<UiState<Terms>> = state

    fun loadTerms() {
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