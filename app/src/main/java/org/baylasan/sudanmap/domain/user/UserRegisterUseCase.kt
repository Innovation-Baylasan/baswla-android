package org.baylasan.sudanmap.domain.user

import io.reactivex.Single
import org.baylasan.sudanmap.data.user.model.RegisterRequest
import org.baylasan.sudanmap.data.user.model.AuthenticationResponse
import org.baylasan.sudanmap.domain.common.RequestValues
import org.baylasan.sudanmap.domain.common.SingleUseCase

open class UserRegisterUseCase(private val userRepository: UserRepository) :SingleUseCase<UserRegisterUseCase.Params ,AuthenticationResponse> {

    class Params (val registerRequest: RegisterRequest)  : RequestValues

    override fun execute(params: Params): Single<AuthenticationResponse> {
        return userRepository.register(params.registerRequest)
    }
}


