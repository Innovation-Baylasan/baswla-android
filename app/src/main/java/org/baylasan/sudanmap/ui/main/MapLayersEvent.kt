package org.baylasan.sudanmap.ui.main

import org.baylasan.sudanmap.domain.entity.model.Entity

sealed class EntityEvent

object LoadingEvent : EntityEvent()
object NetworkErrorEvent : EntityEvent()
object EmptyEvent : EntityEvent()
object SessionExpiredEvent : EntityEvent()
object TimeoutEvent : EntityEvent()
class ErrorEvent(val errorMessage: String) : EntityEvent()
class DataEvent(val entities: List<Entity>) : EntityEvent()