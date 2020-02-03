package org.baylasan.sudanmap.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.baylasan.sudanmap.data.user.model.UserDto
import org.baylasan.sudanmap.domain.user.SessionManager

class UserProfileViewModel(private val sessionManager: SessionManager) : ViewModel() {
    private val userProfileLiveData = MutableLiveData<UserDto?>()

    fun loadUser() {
        userProfileLiveData.value = if (sessionManager.isGuest())
            null
        else
            sessionManager.getUser()
    }

    fun listenToUserProfile() = userProfileLiveData

    fun isThisMine(id: Int): Boolean {
        return id == sessionManager.getUser().id
    }
}
