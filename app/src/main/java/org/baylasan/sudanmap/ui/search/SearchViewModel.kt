package org.baylasan.sudanmap.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.domain.entity.FindEntitiesByKeywordUseCase
import org.baylasan.sudanmap.ui.BaseViewModel
import org.baylasan.sudanmap.ui.main.*
import java.util.concurrent.TimeUnit

class SearchViewModel(private val useCase: FindEntitiesByKeywordUseCase) : BaseViewModel() {
    val events = MutableLiveData<EntityEvent>()
    private val searchSubject = BehaviorSubject.create<String>()

    init {
        searchSubject
            .filter { it.isNotEmpty() && it.isNotBlank() && it.length > 1 }
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { keyword ->
                events.value = LoadingEvent
                useCase.execute(FindEntitiesByKeywordUseCase.Request(keyword))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.isEmpty()) {
                            events.value = EmptyEvent
                        } else {
                            events.value = DataEvent(it)
                        }
                    }, {
                        it.printStackTrace()
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
            }.addToDisposables()


    }


    fun findEntitiesWithKeyword(key: String) {
        if(key.isEmpty())
            return
        Log.d("MEGA", "new search term $key")
        searchSubject.onNext(key)


    }

}