package org.baylasan.sudanmap.ui.faq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.domain.faq.GetFaqUseCase
import org.baylasan.sudanmap.domain.faq.model.Faq
import org.baylasan.sudanmap.domain.faq.model.Faqs
import org.baylasan.sudanmap.domain.policy.model.Policy
import org.baylasan.sudanmap.ui.BaseViewModel

class FaqViewModel(private val useCase: GetFaqUseCase) :BaseViewModel(){
    private val state = MutableLiveData<UiState<List<Faq>>>()
    val uiState: LiveData<UiState<List<Faq>>> = state

    fun loadFaqs() {
        useCase.execute()
            .compose {
                state.value = UiState.Loading()
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                state.value = UiState.Success(it.data)
            }, {
                state.value = UiState.Error(it)
            })
            .addToDisposables()
    }
}