package org.baylasan.sudanmap.ui.layers

import org.baylasan.sudanmap.data.entity.model.Category

sealed class MapLayersEvent

object LoadingEvent : MapLayersEvent()
object NetworkErrorEvent : MapLayersEvent()
object EmptyEvent : MapLayersEvent()
object SessionExpiredEvent : MapLayersEvent()
object TimeoutEvent : MapLayersEvent()
class ErrorEvent(val errorMessage: String) : MapLayersEvent()
class DataEvent(val categories: List<Category>) : MapLayersEvent()


