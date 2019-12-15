package org.baylasan.sudanmap.ui.layers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.domain.category.FetchCategoriesUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class MapLayersViewModel(private val fetchCategoriesUseCase: FetchCategoriesUseCase) :
    BaseViewModel() {

    val events = MutableLiveData<MapLayersEvent>()
    override fun onCleared() {
        super.onCleared()
        Log.d("MEGA","Cleared.")
    }
    fun loadCategories() {
        events.value = LoadingEvent
        fetchCategoriesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                events.value = if (it.isNotEmpty()) {
                    DataEvent(it)
                } else {
                    EmptyEvent
                }
            }, {
                events.value = when (it) {
                    is UnAuthorizedException -> {
                        SessionExpiredEvent
                    }
                    is TimeoutConnectionException -> {
                        TimeoutEvent
                    }
                    is ConnectionException,
                    is ClientConnectionException -> {
                        NetworkErrorEvent
                    }
                    is ApiException -> {
                        ErrorEvent(it.apiErrorResponse.message)
                    }
                    else -> {
                        ErrorEvent("Unexpected errror")
                    }
                }
            }).addToDisposables()

    }
}

