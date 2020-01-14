package org.baylasan.sudanmap.ui.auth.signup

import org.baylasan.sudanmap.data.user.model.RegisterResponse

sealed class RegisterEvent

object LoadingEvent : RegisterEvent()
object NetworkErrorEvent : RegisterEvent()
object EmptyEvent : RegisterEvent()
object SessionExpiredEvent : RegisterEvent()
object TimeoutEvent : RegisterEvent()
class ErrorEvent(val errorMessage: String?) : RegisterEvent()
class DataEvent(val response: RegisterResponse) : RegisterEvent()