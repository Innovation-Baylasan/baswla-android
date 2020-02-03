package org.baylasan.sudanmap.ui.layers

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.entity.model.Category
import org.baylasan.sudanmap.domain.category.FetchCategoriesUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class MapLayersViewModel(private val fetchCategoriesUseCase: FetchCategoriesUseCase) :
    BaseViewModel() {

    val events = MutableLiveData<UiState<List<Category>>>()

    fun loadCategories() {
        events.value = UiState.Loading()
        fetchCategoriesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                events.value = if (it.isNotEmpty()) {
                    UiState.Success(it)
                } else {
                    UiState.Empty()
                }
            }, {
                events.value = UiState.Error(it)
            }).addToDisposables()

    }
}

