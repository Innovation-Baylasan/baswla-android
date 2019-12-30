package org.baylasan.sudanmap.ui.search

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.domain.entity.FindEntitiesByKeywordUseCase
import org.baylasan.sudanmap.domain.entity.FindEntitiesByKeywordUseCase.Request
import org.baylasan.sudanmap.ui.BaseViewModel
import org.baylasan.sudanmap.ui.main.*

class SearchViewModel(private val useCase: FindEntitiesByKeywordUseCase) : BaseViewModel() {
    val events = MutableLiveData<EntityEvent>()

    fun findEntitiesWithKeyword(key: String) {
        events.value = LoadingEvent

        useCase.execute(Request(key))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    events.value = EmptyEvent
                } else {
                    events.value = DataEvent(it)
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
                        ErrorEvent("Unexpected error")
                    }
                }
            }).addToDisposables()

    }

}