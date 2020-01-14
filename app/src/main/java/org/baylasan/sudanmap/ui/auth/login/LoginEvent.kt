package org.baylasan.sudanmap.ui.auth.login

import org.baylasan.sudanmap.data.user.model.LoginResponse

sealed class LoginEvent

object LoadingEvent : LoginEvent()
object NetworkErrorEvent : LoginEvent()
object EmptyEvent : LoginEvent()
object SessionExpiredEvent : LoginEvent()
object TimeoutEvent : LoginEvent()
class ErrorEvent(val errorMessage: String?) : LoginEvent()
class DataEvent(val response: LoginResponse) : LoginEvent()