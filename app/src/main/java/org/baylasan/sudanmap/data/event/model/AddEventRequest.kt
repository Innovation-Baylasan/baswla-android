package org.baylasan.sudanmap.data.event.model

class AddEventRequest(
    private val name: String,
    private val description: String,
    private val address: String,
    private val cover: String,
    private val seats: Int,
    private val price: Double,
    private val registrationLink: String,
    private val startDateTime: Long,
    private val endDateTime: Long,
    private val entityId: Int,
    private val location: String
)