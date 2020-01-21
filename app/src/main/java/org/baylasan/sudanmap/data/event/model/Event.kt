package org.baylasan.sudanmap.data.event.model


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.data.entity.model.Location

@Keep
@Parcelize
data class Event(
    @SerializedName("application_end_datetime") val applicationEndDatetime: String,
    @SerializedName("application_start_datetime") val applicationStartDatetime: String,
    @SerializedName("creator") val creator: Creator,
    @SerializedName("description") val description: String,
    @SerializedName("entity") val entity: Entity,
    @SerializedName("event_name") val eventName: String,
    @SerializedName("event_picture") val eventPicture: String,
    @SerializedName("id") val id: Int,
    @SerializedName("location") val location: Location,
    @SerializedName("registration_link") val registrationLink: String
) : Parcelable