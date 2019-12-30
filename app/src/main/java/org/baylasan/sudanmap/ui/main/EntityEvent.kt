package org.baylasan.sudanmap.ui.main

import org.baylasan.sudanmap.data.entity.model.EntityDto

sealed class EntityEvent

object LoadingEvent : EntityEvent()
object NetworkErrorEvent : EntityEvent()
object EmptyEvent : EntityEvent()
object SessionExpiredEvent : EntityEvent()
object TimeoutEvent : EntityEvent()
class ErrorEvent(val errorMessage: String) : EntityEvent()
class DataEvent(val entityDtos: List<EntityDto>) : EntityEvent()