package org.baylasan.sudanmap.ui.addentity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.baylasan.sudanmap.common.UiState
import org.baylasan.sudanmap.data.common.AddEntityResponseException
import org.baylasan.sudanmap.data.common.UnAuthorizedException
import org.baylasan.sudanmap.data.entity.model.AddEntityRequest
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.data.entity.model.Tag
import org.baylasan.sudanmap.domain.entity.AddEntityUseCase
import org.baylasan.sudanmap.domain.tags.GetTagsByNameUseCase
import org.baylasan.sudanmap.domain.tags.GetTagsUseCase
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.ui.BaseViewModel

class AddEntityViewModel(
    private val useCase: AddEntityUseCase,
    private val sessionManager: SessionManager,
    private val tagsUseCase: GetTagsUseCase,
    private val tagsByNameUseCase: GetTagsByNameUseCase
) : BaseViewModel() {

    private val addUiState = MutableLiveData<UiState<Entity>>()
    private val tagsUiState = MutableLiveData<UiState<List<Tag>>>()
    val addState: LiveData<UiState<Entity>> = addUiState
    val tagsState: LiveData<UiState<List<Tag>>> = tagsUiState
    fun add(addEntityRequest: AddEntityRequest) {
        addUiState.value = UiState.Loading()
        useCase.execute(AddEntityUseCase.Request(addEntityRequest))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                addUiState.value = UiState.Complete()
            }, {
                if (it is UnAuthorizedException) {
                    sessionManager.clear()
                }

                addUiState.value = UiState.Error(it )
            })
            .addToDisposables()
    }

    fun getTags(name: String = "") {
        val disposable: Disposable = if (name.isEmpty()) {
            tagsUseCase.execute()
                .compose {
                    tagsUiState.value = UiState.Loading()
                    it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    tagsUiState.value = UiState.Success(it)
                }, {
                    tagsUiState.value = UiState.Error(it)
                })
        } else {
            tagsByNameUseCase.execute(GetTagsByNameUseCase.Request(name))
                .compose {
                    tagsUiState.value = UiState.Loading()
                    it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    tagsUiState.value = UiState.Success(it)
                }, {
                    tagsUiState.value = UiState.Error(it)
                })
        }
        disposable.addToDisposables()
    }
}