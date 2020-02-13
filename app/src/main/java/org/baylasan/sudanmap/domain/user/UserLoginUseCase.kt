package org.baylasan.sudanmap.domain.user

import io.reactivex.Single
import org.baylasan.sudanmap.data.user.model.AuthenticationResponse
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.LoginResponse
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase
import java.util.regex.Pattern

open class UserLoginUseCase(private val userRepository: UserRepository) :
    SingleUseCase<UserLoginUseCase.Params, AuthenticationResponse> {

    class Params(val loginRequest: LoginRequest) : RequestValues

    override fun execute(params: Params): Single<AuthenticationResponse> {
        val request = params.loginRequest
        return when {
            request.email.isEmpty() -> Single.error(EmptyFieldException())
            !request.email.matches(EMAIL_ADDRESS_PATTERN.toRegex()) -> Single.error(InvalidEmailAddressException())
            request.password.isEmpty() -> Single.error(EmptyFieldException())
            else -> userRepository.login(request)
        }
    }
}

class InvalidEmailAddressException : Exception()
class EmptyFieldException : Exception()

val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)