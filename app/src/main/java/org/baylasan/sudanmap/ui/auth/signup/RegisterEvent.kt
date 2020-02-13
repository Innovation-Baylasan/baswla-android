package org.baylasan.sudanmap.ui.auth.signup

import org.baylasan.sudanmap.data.user.model.AuthenticationResponse

sealed class RegisterEvent

object LoadingEvent : RegisterEvent()
object NetworkErrorEvent : RegisterEvent()
object TimeoutEvent : RegisterEvent()
class ErrorEvent(val errorMessage: String?) : RegisterEvent()
object DataEvent : RegisterEvent()