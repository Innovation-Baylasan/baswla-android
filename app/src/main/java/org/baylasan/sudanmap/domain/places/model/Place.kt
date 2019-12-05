package m7mdra.com.mawgif.domain.places.model

import com.google.gson.annotations.SerializedName
import java.math.BigInteger
import java.util.*

data class Place(
        @SerializedName("place_id") val placeId: Int,
        @SerializedName("address") val address: String,
        @SerializedName("user_id") val userId: Int,
        @SerializedName("size") val size: Double,
        @SerializedName("longitude") val longitude: Double,
        @SerializedName("latitude") val latitude: Double,
        @SerializedName("description") val description: String,
        @SerializedName("cost") val cost: BigInteger,
        @SerializedName("status_id") val statusId: Int,
        @SerializedName("created_at") val createdAt: Date,
        @SerializedName("updated_at") val updateAt: Date)


/*
{
        "place_id": 3,
        "address": "شارع عبيد ختم",
        "user_id": 2,
        "size": 22,
        "longitude": 15.5732231,
        "latitude": 32.5594938,
        "description": "مشاحة صغيرة",
        "cost": 15,
        "status_id": 1,
        "created_at": "2018-06-21T13:06:33.000Z",
        "updated_at": "2018-06-21T13:06:33.000Z"
    }

*/