package org.baylasan.sudanmap.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.baylasan.sudanmap.domain.user.SessionManager

class SessionViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val sessionLiveData = MutableLiveData<SessionEvent>()
    fun checkSession() {
        val isFirstTime = sessionManager.isFirstTime()
        val isLoggedIn = sessionManager.isLoggedIn()
        if (isFirstTime) {

            sessionLiveData.value = FirstTimeLaunch
        } else {
            if (isLoggedIn) {
                sessionLiveData.value = AuthenticatedUser
            } else {
                sessionLiveData.value = UnauthenticatedUser

            }
        }
    }
}