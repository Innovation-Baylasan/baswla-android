package org.baylasan.sudanmap.ui.auth.login

sealed class LoginEvent

object LoadingEvent : LoginEvent()
object NetworkErrorEvent : LoginEvent()
object TimeoutEvent : LoginEvent()
class ValidationErrorEvent (val message:Int): LoginEvent()
class ErrorEvent(val errorMessage: String?) : LoginEvent()
object DataEvent : LoginEvent()