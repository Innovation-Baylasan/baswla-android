package org.baylasan.sudanmap.ui.layers

import org.baylasan.sudanmap.domain.entity.model.Category

sealed class MapLayersEvent {}

class LoadingEvent : MapLayersEvent()
class NetworkErrorEvent : MapLayersEvent()
class EmptyEvent : MapLayersEvent()
class SessionExpiredEvent : MapLayersEvent()
class TimeoutEvent : MapLayersEvent()
class ErrorEvent(val errorMessage: String) : MapLayersEvent()
class DataEvent(val categories: List<Category>) : MapLayersEvent()