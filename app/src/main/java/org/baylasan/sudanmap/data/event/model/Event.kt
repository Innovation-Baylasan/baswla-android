package org.baylasan.sudanmap.data.event.model


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.data.entity.model.EntityDetails
import org.baylasan.sudanmap.data.entity.model.Location

@Keep
@Parcelize
data class Event(
    @SerializedName("end_date") val endDatetime: String?,
    @SerializedName("start_date") val startDatetime: String?,
    @SerializedName("creator") val creator: Creator?,
    @SerializedName("description") val description: String?,
    @SerializedName("entity") val entity:EntityDetails?,
    @SerializedName("name") val name: String,
    @SerializedName("picture") val picture: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("location") val location: Location?,
    @SerializedName("registration_link") val registrationLink: String?
) : Parcelable