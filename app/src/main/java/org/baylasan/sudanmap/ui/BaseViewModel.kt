package org.baylasan.sudanmap.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class BaseViewModel : ViewModel() {
    val disposables = CompositeDisposable()
}