package org.baylasan.sudanmap.data.event.model

import com.google.gson.annotations.SerializedName

class AddEventRequest(
    @SerializedName("event_name")
    private val name: String,
    @SerializedName("description")
    private val description: String,
    @SerializedName("event_picture")
    private val cover: String,
    @SerializedName("registration_link")
    private val registrationLink: String,
    @SerializedName("application_start_datetime")
    private val startDateTime: Long,
    @SerializedName("application_end_datetime")
    private val endDateTime: Long,
    @SerializedName("entity_id")
    private val entityId: Int,
    @SerializedName("latitude")
    private val locationLat: Double,
    @SerializedName("longitude")
    private val locationLng: Double,

    private val address: String = "",
    private val seats: Int = 0,
    private val price: Double = 0.0

)