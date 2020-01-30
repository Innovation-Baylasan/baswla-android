package org.baylasan.sudanmap.domain.user

import org.baylasan.sudanmap.data.user.model.UserDto

interface SessionManager {
    fun saveUserSession(user: UserDto)
    fun getUser(): UserDto
    fun getToken(): String
    fun isLoggedIn(): Boolean
    fun setLoggedIn(loggedIn: Boolean)
    fun setIsFirstTime(firstTime: Boolean)
    fun isFirstTime(): Boolean
    fun isGuest(): Boolean
    fun clear()
}