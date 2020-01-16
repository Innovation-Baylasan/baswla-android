package org.baylasan.sudanmap.ui.splash

sealed class SessionEvent
object FirstTimeLaunch : SessionEvent()
object AuthenticatedUser : SessionEvent()
object UnauthenticatedUser : SessionEvent()
