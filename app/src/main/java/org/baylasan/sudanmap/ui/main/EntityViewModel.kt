package org.baylasan.sudanmap.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.domain.entity.GetEntitiesUseCase
import org.baylasan.sudanmap.domain.entity.model.Category
import org.baylasan.sudanmap.domain.entity.model.Entity
import org.baylasan.sudanmap.ui.BaseViewModel

class EntityViewModel(private val getEntitiesUseCase: GetEntitiesUseCase) : BaseViewModel() {

    lateinit var entities: List<Entity>
    val events = MutableLiveData<EntityEvent>()
    val filterLiveData = MutableLiveData<List<Category>>()


    @SuppressLint("CheckResult")
    fun loadEntity() {
        events.value = LoadingEvent

        getEntitiesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                entities = it

                if (it.isNotEmpty()) {
                    DataEvent(it)
                    events.value = DataEvent(it)

                    filterLiveData.value =
                        entities.groupBy { entity -> entity.category }.keys.toMutableList().apply {
                            add(0, Category("", "", -1, "All", ""))
                        }
                } else {
                    events.value = EmptyEvent
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
                        ErrorEvent("Unexpected errror")
                    }
                }
            }).addToDisposables()
    }

    fun filterEntities(category: Category) {
        val list = entities.filter { it.category == category }
        if (category.id == -1) {
            events.value = DataEvent(entities)

        } else {
            events.value = DataEvent(list)
        }
    }

}