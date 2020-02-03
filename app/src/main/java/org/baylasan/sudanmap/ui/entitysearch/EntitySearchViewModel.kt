package org.baylasan.sudanmap.ui.entitysearch

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.entity.FindEntitiesByKeywordUseCase
import org.baylasan.sudanmap.ui.BaseViewModel
import org.baylasan.sudanmap.ui.main.entity.*

class EntitySearchViewModel(private val useCase: FindEntitiesByKeywordUseCase) : BaseViewModel() {
    val events = MutableLiveData<UiState<List<Entity>>>()



    fun findEntitiesWithKeyword(key: String) {
        events.value = UiState.Loading()
        useCase.execute(FindEntitiesByKeywordUseCase.Request(key))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    events.value = UiState.Empty()
                } else {
                    events.value =
                        UiState.Success(it)
                }
            }, {
                events.value = UiState.Error(it)
            }).addToDisposables()


    }

}