package org.baylasan.sudanmap.domain.user

import io.reactivex.Single
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.LoginResponse
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class UserLoginUseCase(private val userRepository: UserRepository) :
    SingleUseCase<UserLoginUseCase.Params, LoginResponse> {

    class Params(val loginRequest: LoginRequest) : RequestValues

    override fun execute(params: Params): Single<LoginResponse> {
        return userRepository.login(params.loginRequest)
    }
}