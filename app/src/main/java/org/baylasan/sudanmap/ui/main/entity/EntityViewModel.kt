package org.baylasan.sudanmap.ui.main.entity

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.common.*
import org.baylasan.sudanmap.data.entity.model.Category
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.domain.entity.GetEntitiesUseCase
import org.baylasan.sudanmap.domain.entity.GetNearbyEntitiesUseCase
import org.baylasan.sudanmap.ui.BaseViewModel

class EntityViewModel(
    private val getEntitiesUseCase: GetEntitiesUseCase,
    private val getNearbyEntitiesUseCase: GetNearbyEntitiesUseCase
) : BaseViewModel() {

    lateinit var entities: List<Entity>
    val events = MutableLiveData<UiState<List<Entity>>>()

    val nearbyEvents = MutableLiveData<UiState<List<Entity>>>()

    val filterLiveData = MutableLiveData<List<Category>>()


    fun loadEntity() {
        events.value = UiState.Loading()

        getEntitiesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                entities = it

                if (it.isNotEmpty()) {
                    events.value = UiState.Success(it)

                    filterLiveData.value =
                        it.groupBy { entity -> entity.category }.keys.toMutableList()

                } else {
                    events.value = UiState.Empty()
                }

            }, {
                it.printStackTrace()
                events.value = UiState.Error(it)
            }).addToDisposables()
    }


    fun loadNearby(latitude: Double, longitude: Double) {
        nearbyEvents.value =UiState.Loading()
        val params = GetNearbyEntitiesUseCase.Params(latitude, longitude)
        getNearbyEntitiesUseCase.execute(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty()) {
                    nearbyEvents.value = UiState.Success(it)
                } else {
                    nearbyEvents.value =UiState.Empty()
                }
            }, {
                it.printStackTrace()

                nearbyEvents.value = UiState.Error(it)
            }).addToDisposables()
    }

    fun filterEntities(category: Category) {
        val list = entities.filter { it.category == category }
        if (category.id == -1) {
            events.value = UiState.Success(entities)

        } else {
            events.value = UiState.Success(list)
        }
    }

}