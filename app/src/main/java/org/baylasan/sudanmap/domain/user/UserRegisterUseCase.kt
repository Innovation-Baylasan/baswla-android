package org.baylasan.sudanmap.domain.user

import io.reactivex.Single
import org.baylasan.sudanmap.data.user.model.LoginRequest
import org.baylasan.sudanmap.data.user.model.LoginResponse
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.data.user.model.RegisterResponse
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

class UserRegisterUseCase(private val userRepository: UserRepository) :SingleUseCase<UserRegisterUseCase.Params ,RegisterResponse> {

    class Params (val registerRequest: RegisterRequest)  : RequestValues

    override fun execute(params: Params): Single<RegisterResponse> {
        return userRepository.register(params.registerRequest)
    }
}


