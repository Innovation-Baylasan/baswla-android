package org.baylasan.sudanmap.ui.entitydetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.entity.model.EntityDetails
import org.baylasan.sudanmap.data.entity.model.Review
import org.baylasan.sudanmap.domain.entity.*
import org.baylasan.sudanmap.ui.BaseViewModel

class EntityDetailsViewModel(
    private val getEntityDetailsUseCase: GetEntityDetailsUseCase,
    private val followEntityUseCase: FollowEntityUseCase,
    private val unFollowEntityUseCase: UnFollowEntityUseCase,
    private val rateEntityUseCase: RateEntityUseCase,
    private val addReviewUseCase: AddReviewUseCase
) : BaseViewModel() {
    private val loadEntityUiState = MutableLiveData<UiState<EntityDetails>>()
    private val reviewUiState = MutableLiveData<UiState<Review>>()
    private val followUiState = MutableLiveData<UiState<Unit>>()
    private val unFollowUiState = MutableLiveData<UiState<Unit>>()
    private val rateUiState = MutableLiveData<UiState<Unit>>()

    val entityDetailsState: LiveData<UiState<EntityDetails>> = loadEntityUiState
    val reviewState: LiveData<UiState<Review>> = reviewUiState
    val followState: LiveData<UiState<Unit>> = followUiState
    val unFollowState: LiveData<UiState<Unit>> = unFollowUiState
    val rateState: LiveData<UiState<Unit>> = rateUiState

    fun getDetailsForId(id: Int) {
        loadEntityUiState.value = UiState.Loading()
        getEntityDetailsUseCase.execute(GetEntityDetailsUseCase.Request(id))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadEntityUiState.value = UiState.Success(it)

            }, {
                loadEntityUiState.value = UiState.Error(it)
            })
            .addToDisposables()
    }

    fun follow(id: Int) {
        followUiState.value = UiState.Loading()
        followEntityUseCase.execute(FollowEntityUseCase.Request(id))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                followUiState.value = UiState.Complete()

            }, {
                followUiState.value = UiState.Error(it)

            })
            .addToDisposables()

    }

    fun unFollow(id: Int) {
        unFollowUiState.value = UiState.Loading()
        unFollowEntityUseCase.execute(UnFollowEntityUseCase.Request(id))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                unFollowUiState.value = UiState.Complete()

            }, {
                unFollowUiState.value = UiState.Error(it)

            })
            .addToDisposables()
    }

    fun review(content: String, id: Int) {
        reviewUiState.value = UiState.Loading()
        addReviewUseCase.execute(AddReviewUseCase.Request(id, content))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                reviewUiState.value = UiState.Success(it)
            }, {
                reviewUiState.value = UiState.Error(it)
            }).addToDisposables()

    }

    fun rate(id: Int, rating: Float) {
        rateUiState.value = UiState.Loading()
        rateEntityUseCase.execute(RateEntityUseCase.Request(id, rating.toDouble()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                rateUiState.value = UiState.Complete()

            }, {
                rateUiState.value = UiState.Error(it)

            }).addToDisposables()
    }


}