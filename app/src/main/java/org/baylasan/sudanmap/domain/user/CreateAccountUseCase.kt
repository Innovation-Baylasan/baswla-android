package m7mdra.com.mawgif.domain.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import m7mdra.com.mawgif.domain.common.RequestValues
import m7mdra.com.mawgif.domain.common.SingleUseCase
import m7mdra.com.mawgif.domain.user.model.User

class CreateAccountUseCase(private val userRepository: UserRepository) :
        SingleUseCase<CreateAccountUseCase.Request, User> {
    override fun execute(params: Request): Single<User> {
        return userRepository.createUser(params)
    }

    data class Request(@Expose @SerializedName("phone") val phoneNumber: String,
                       @Expose @SerializedName("name") val name: String,
                       @Expose @SerializedName("email") val email: String,
                       @Expose @SerializedName("password") val password: String) : RequestValues
}