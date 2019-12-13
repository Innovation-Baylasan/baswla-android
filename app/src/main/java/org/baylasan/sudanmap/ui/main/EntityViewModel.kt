package org.baylasan.sudanmap.ui.main

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.domain.entity.GetEntitiesUseCase

class EntityViewModel(private val getEntitiesUseCase: GetEntitiesUseCase) : ViewModel() {
    fun loadEntity(categoryId: Int) {
        getEntitiesUseCase.execute(GetEntitiesUseCase.Params(categoryId)).subscribeOn(AndroidSchedulers.mainThread()).observeOn(
            Schedulers.io()).subscribe { {} , {} }
    }
}