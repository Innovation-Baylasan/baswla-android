package org.baylasan.sudanmap.data.event.model

import java.io.File


class AddEventRequest(
     val name: String,
     val description: String,
     val cover: File?,
     val registrationLink: String,
     val startDateTime: String,
     val endDateTime: String,
     val entityId: Int?,
     val locationLat: Double,
     val locationLng: Double
/*
     val address: String = "",
     val seats: Int = 0,
     val price: Double = 0.0*/

)