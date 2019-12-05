package m7mdra.com.mawgif.domain.user.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class User(@SerializedName("user_id") val userId: String,
                @SerializedName("phone") val phoneNumber: String,
                @SerializedName("name") val name: String,
                @SerializedName("email") val email: String,
                @SerializedName("password") val password: String,
                @SerializedName("created_at") val creationDate: Date,
                @SerializedName("updated_at") val updateDate: Date

)