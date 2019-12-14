package org.baylasan.sudanmap.ui.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.domain.entity.GetEntitiesUseCase
import org.baylasan.sudanmap.domain.entity.model.EntityResponseDto

class EntityViewModel(private val getEntitiesUseCase: GetEntitiesUseCase) : ViewModel() {

    private val _entity = MutableLiveData<EntityResponseDto>()

    val entities :LiveData<EntityResponseDto> = _entity

    @SuppressLint("CheckResult")
    fun loadEntity() {
        getEntitiesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _entity.postValue(it)
                Log.d("MEGA", it.toString())
            }, {
                it.printStackTrace()
            })
    }

}