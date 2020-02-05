package org.baylasan.sudanmap.ui.intro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.baylasan.sudanmap.domain.user.SessionManager

class IntroViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val event = MutableLiveData<Unit>()
    fun setFirstLaunchCompleted() {
        sessionManager.setIsFirstTime(false)
        event.value = Unit
    }
}