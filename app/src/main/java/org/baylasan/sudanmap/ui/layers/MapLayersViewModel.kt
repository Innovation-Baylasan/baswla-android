package org.baylasan.sudanmap.ui.layers

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.domain.category.FetchCategoriesUseCase

class MapLayersViewModel(private val fetchCategoriesUseCase: FetchCategoriesUseCase) : ViewModel() {

    @SuppressLint("CheckResult")
    fun loadCategories() {
        fetchCategoriesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("MEGA", it.toString())
            }, {
                it.printStackTrace()
            })
    }
}