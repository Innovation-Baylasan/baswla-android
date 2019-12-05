package m7mdra.com.mawgif.domain.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import m7mdra.com.mawgif.domain.common.RequestValues
import m7mdra.com.mawgif.domain.common.SingleUseCase
import m7mdra.com.mawgif.domain.user.model.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val userRepository: UserRepository) : SingleUseCase<LoginUseCase.Request, User> {
    override fun execute(params: Request): Single<User> {
        return userRepository.login(params);
    }

    data class Request(@Expose @SerializedName("email") val email: String,
                       @Expose @SerializedName("password") val password: String) : RequestValues
}