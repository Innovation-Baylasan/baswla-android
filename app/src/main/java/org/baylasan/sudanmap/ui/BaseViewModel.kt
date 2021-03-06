package org.baylasan.sudanmap.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    fun Disposable.addToDisposables() {
        disposables.add(this)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

}