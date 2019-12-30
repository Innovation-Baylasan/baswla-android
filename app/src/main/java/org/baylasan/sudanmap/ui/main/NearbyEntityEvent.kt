package org.baylasan.sudanmap.ui.main

import org.baylasan.sudanmap.data.entity.model.NearByEntity

sealed class NearbyEntityEvent

object NearbyLoadingEvent : NearbyEntityEvent()
object NearbyNetworkErrorEvent : NearbyEntityEvent()
object NearbyEmptyEvent : NearbyEntityEvent()
object NearbySessionExpiredEvent : NearbyEntityEvent()
object NearbyTimeoutEvent : NearbyEntityEvent()
class NearbyErrorEvent(val errorMessage: String) : NearbyEntityEvent()
class NearbyDataEvent(val nearByEntity: NearByEntity) : NearbyEntityEvent()