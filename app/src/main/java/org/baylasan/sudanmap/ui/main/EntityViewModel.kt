package org.baylasan.sudanmap.ui.main

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.entity.model.Category
import org.baylasan.sudanmap.data.entity.model.EntityDto
import org.baylasan.sudanmap.domain.entity.GetEntitiesUseCase
import org.baylasan.sudanmap.domain.entity.GetNearbyEntitiesUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class EntityViewModel(
    private val getEntitiesUseCase: GetEntitiesUseCase,
    private val getNearbyEntitiesUseCase: GetNearbyEntitiesUseCase
) : BaseViewModel() {

    private lateinit var entityDto: List<EntityDto>

    val events = MutableLiveData<EntityEvent>()

    val nearbyEvents = MutableLiveData<NearbyEntityEvent>()

    val filterLiveData = MutableLiveData<List<Category>>()


    fun loadEntity() {
        events.value = LoadingEvent

        getEntitiesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                entityDto = it

                if (it.isNotEmpty()) {
                    DataEvent(it)
                    events.value = DataEvent(it)

                    filterLiveData.value =
                        entityDto.groupBy { entity -> entity.category }.keys.toMutableList()
                            .apply {
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


    fun loadNearby(latitude: Double, longitude: Double) {
        nearbyEvents.value = NearbyLoadingEvent
        val params = GetNearbyEntitiesUseCase.Params(latitude, longitude)
        getNearbyEntitiesUseCase.execute(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ nearbyEvents.value = NearbyDataEvent(it) }, {
                it.printStackTrace()

                nearbyEvents.value = when (it) {
                    is UnAuthorizedException -> {
                        NearbySessionExpiredEvent
                    }
                    is TimeoutConnectionException -> {
                        NearbyTimeoutEvent
                    }
                    is ConnectionException,
                    is ClientConnectionException -> {
                        NearbyNetworkErrorEvent
                    }
                    is ApiException -> {
                        NearbyErrorEvent(it.apiErrorResponse.message)
                    }
                    else -> {
                        NearbyErrorEvent("Unexpected errror")
                    }
                }
            }).addToDisposables()
    }

    fun filterEntities(category: Category) {
        val list = entityDto.filter { it.category == category }
        if (category.id == -1) {
            events.value = DataEvent(entityDto)

        } else {
            events.value = DataEvent(list)
        }
    }

}