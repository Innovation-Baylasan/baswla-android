package org.baylasan.sudanmap.ui.main

import org.baylasan.sudanmap.domain.entity.model.EntityResponseDto

sealed class EntityEvent

object LoadingEvent : EntityEvent()
object NetworkErrorEvent : EntityEvent()
object EmptyEvent : EntityEvent()
object SessionExpiredEvent : EntityEvent()
object TimeoutEvent : EntityEvent()
class ErrorEvent(val errorMessage: String) : EntityEvent()
class DataEvent(val entityResponseDto: EntityResponseDto) : EntityEvent()