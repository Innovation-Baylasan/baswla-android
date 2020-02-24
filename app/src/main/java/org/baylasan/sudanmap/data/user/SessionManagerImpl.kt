package org.baylasan.sudanmap.data.user

import android.content.SharedPreferences
import com.google.gson.Gson
import org.baylasan.sudanmap.common.clear
import org.baylasan.sudanmap.common.findPreference
import org.baylasan.sudanmap.common.putPreference
import org.baylasan.sudanmap.data.entity.model.EntityDetails
import org.baylasan.sudanmap.data.user.model.UserDto
import org.baylasan.sudanmap.domain.user.SessionManager

class SessionManagerImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SessionManager {


    override fun saveUserSession(user: UserDto) {
        setIsFirstTime(false)
        sharedPreferences.putPreference(KEY_USER_ID, user.id)
        sharedPreferences.putPreference(KEY_USER_EMAIL, user.email)
        sharedPreferences.putPreference(KEY_USER_TOKEN, user.token)
        sharedPreferences.putPreference(KEY_NAME, user.name)
        sharedPreferences.putPreference(KEY_IS_LOGIN, true)
        sharedPreferences.putPreference(KEY_ROLE, user.role)
        sharedPreferences.putPreference(KEY_USER_NAME, user.username)
    }

    override fun getUser(): UserDto {
        return UserDto(
            email = sharedPreferences.findPreference(KEY_USER_EMAIL, ""),
            id = sharedPreferences.findPreference(KEY_USER_ID, 0),
            token = sharedPreferences.findPreference(KEY_USER_TOKEN, ""),
            name = sharedPreferences.findPreference(KEY_NAME, ""),
            username = sharedPreferences.findPreference(KEY_USER_NAME, ""),
            role = sharedPreferences.findPreference(KEY_ROLE, "user")

        )
    }

    override fun getToken(): String = sharedPreferences.findPreference(KEY_USER_TOKEN, "")

    override fun isLoggedIn(): Boolean = sharedPreferences.findPreference(KEY_IS_LOGIN, false)

    override fun setLoggedIn(loggedIn: Boolean) =
        sharedPreferences.putPreference(KEY_IS_LOGIN, loggedIn)

    override fun saveEntity(entityDetails: EntityDetails) {
        sharedPreferences.edit().putString(KEY_ENTITY, gson.toJson(entityDetails)).apply()
    }

    override fun getEntity(): EntityDetails? {
        return gson.fromJson(sharedPreferences.getString(KEY_ENTITY, ""), EntityDetails::class.java)
    }

    override fun setIsFirstTime(firstTime: Boolean) =
        sharedPreferences.putPreference(KEY_IS_FIRST_TIME, firstTime)

    override fun isFirstTime(): Boolean = sharedPreferences.findPreference(KEY_IS_FIRST_TIME, true)
    override fun isGuest(): Boolean = !sharedPreferences.contains(KEY_USER_TOKEN)
    override fun isCompany() = sharedPreferences.getString(KEY_ROLE, "user") == "company"


    override fun isUser() =
        sharedPreferences.getString(KEY_ROLE, "user") == "Company"


    override fun clear() {
      sharedPreferences.clear()
    }

    companion object {
        private const val KEY_PREFERENCE_FILE_NAME = "baswala"
        private const val KEY_IS_LOGIN = "is_login"
        private const val KEY_IS_FIRST_TIME = "first_time"
        private const val KEY_ENTITY = "entity"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_NAME = "name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_TOKEN = "user_token"
        private const val KEY_ROLE = "user_role"

    }
}