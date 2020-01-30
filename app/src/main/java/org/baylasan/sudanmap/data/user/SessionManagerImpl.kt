package org.baylasan.sudanmap.data.user

import android.content.Context
import android.content.SharedPreferences
import org.baylasan.sudanmap.common.clear
import org.baylasan.sudanmap.data.user.model.UserDto
import org.baylasan.sudanmap.domain.user.SessionManager
import org.baylasan.sudanmap.common.findPreference
import org.baylasan.sudanmap.common.putPreference

class SessionManagerImpl(private val context: Context) : SessionManager {
    private var prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun saveUserSession(user: UserDto) {
        prefs.putPreference(USER_ID, user.id)
        prefs.putPreference(USER_EMAIL, user.email)
        prefs.putPreference(USER_TOKEN, user.token)
        prefs.putPreference(NAME, user.name)
        prefs.putPreference(IS_LOGIN, true)
        prefs.putPreference(USER_NAME, user.username)
    }

    override fun getUser(): UserDto {
        return UserDto(
            email = prefs.findPreference(USER_EMAIL, ""),
            id = prefs.findPreference(USER_ID, 0),
            token = prefs.findPreference(USER_TOKEN, ""),
            name = prefs.findPreference(NAME, ""),
            username = prefs.findPreference(USER_NAME, "")
        )
    }

    override fun getToken(): String = prefs.findPreference(USER_TOKEN, "")

    override fun isLoggedIn(): Boolean = prefs.findPreference(IS_LOGIN, false)

    override fun setLoggedIn(loggedIn: Boolean) = prefs.putPreference(IS_LOGIN, loggedIn)

    override fun setIsFirstTime(firstTime: Boolean) = prefs.putPreference(IS_FIRST_TIME, firstTime)

    override fun isFirstTime(): Boolean = prefs.findPreference(IS_FIRST_TIME, true)
    override fun isGuest(): Boolean = !prefs.contains(USER_TOKEN)
    override fun clear() {
        prefs.clear()
    }

    companion object {
        private const val PREF_NAME = "baswala"
        private const val IS_LOGIN = "is_login"
        private const val USER_NAME = "user_name"
        private const val NAME = "name"
        private const val USER_EMAIL = "user_email"
        private const val USER_PHONE = "user_phone"
        private const val USER_ID = "user_id"
        private const val USER_TOKEN = "user_token"
        private const val IS_FIRST_TIME = "first_time"

    }
}