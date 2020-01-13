package org.baylasan.sudanmap.ui.main.place

import org.baylasan.sudanmap.data.entity.model.Entity

sealed class EntityEvent

object LoadingEvent : EntityEvent()
object EmptyEvent : EntityEvent()
class DataEvent(val entityList: List<Entity>) : EntityEvent()

object SessionExpiredEvent : EntityEvent()
object TimeoutEvent : EntityEvent()
class ErrorEvent(val errorMessage: String) : EntityEvent()
object NetworkErrorEvent : EntityEvent()
